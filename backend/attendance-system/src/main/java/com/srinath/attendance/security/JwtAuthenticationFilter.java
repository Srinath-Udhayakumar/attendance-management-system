package com.srinath.attendance.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
 ============================================================
  JwtAuthenticationFilter
 ============================================================

 PURPOSE:
 --------
 This filter runs once for every incoming HTTP request.
 It extracts the JWT token from the Authorization header,
 validates it, and sets authentication in Spring Security.

 WHY WE NEED THIS:
 ------------------
 Spring Security does NOT understand JWT automatically.
 So we create this custom filter to:
   1. Read token from request
   2. Validate token
   3. Authenticate user
   4. Set SecurityContext

 This enables protected APIs like:
   - /employees
   - /attendance
   - /admin
 to be accessed only by authenticated users.

 ARCHITECTURE TYPE:
 -------------------
 Stateless Authentication using JWT.
 Server does NOT store session.
 Each request must contain valid token.

 ============================================================
*/

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Service responsible for generating, extracting and validating JWT tokens
    private final JwtService jwtService;

    // Loads user details (username, password, roles) from database
    private final CustomUserDetailsService customUserDetailsService;


    /*
     ============================================================
      doFilterInternal()
     ============================================================

     This method runs for EVERY HTTP request.

     Flow:
     ------
     1. Get Authorization header
     2. Check if it contains Bearer token
     3. Extract token
     4. Extract email from token
     5. Validate token
     6. Set Authentication in SecurityContext
     7. Continue filter chain
    */

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Get Authorization header from request
        // Example header:
        // Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
        final String authHeader = request.getHeader("Authorization");

        final String token;
        final String email;

        // Step 2: Check if header is missing OR does not start with "Bearer "
        // If true → skip authentication and continue request
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3: Extract JWT token
        // Remove "Bearer " (7 characters)
        token = authHeader.substring(7);

        // Step 4: Extract email (subject) from token
        // This reads the payload of JWT
        email = jwtService.extractEmail(token);

        /*
         Step 5:
         Authenticate only if:
         - Email is not null
         - User is NOT already authenticated
        */
        if (email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // Step 6: Load user details from database
            var userDetails =
                    customUserDetailsService.loadUserByUsername(email);

            /*
             Step 7: Validate token
             Validation checks:
                ✔ Signature is correct
                ✔ Token is not expired
                ✔ Email inside token matches database user
            */
            if (jwtService.isTokenValid(token, userDetails.getUsername())) {

                /*
                 Step 8: Create Authentication object

                 UsernamePasswordAuthenticationToken parameters:
                 - Principal → userDetails
                 - Credentials → null (already verified via token)
                 - Authorities → roles (e.g., ROLE_USER, ROLE_ADMIN)
                */
                var authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Attach request details (IP address, session id, etc.)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                /*
                 Step 9: Set authentication in SecurityContext

                 This is the MOST IMPORTANT line.

                 After this:
                 - User is authenticated
                 - @PreAuthorize works
                 - Role-based access works
                 - Security allows protected endpoints
                */
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }
        }

        // Step 10: Continue filter chain
        // If we don't call this, request will stop here.
        filterChain.doFilter(request, response);
    }
}
