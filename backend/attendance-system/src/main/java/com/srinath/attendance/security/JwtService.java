package com.srinath.attendance.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

// Responsible for generating and validating JWT tokens
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes()); //Hash-based Message Authentication Code
    }

    // 🔐 Generate signed Token (contains subject, role, issuedAt, expiration, signature)
    public String generateToken(String email, String role) {

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey()) //HMAC + SHA (256-bit signature)
                .compact();
    }

    // 🔍 Extract Email from token
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // 🔍 Extract Role from token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // 🔍 Validate signature + expiration
    public boolean isTokenValid(String token, String email) {
        String extractedEmail = extractEmail(token);
        return extractedEmail.equals(email) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

