package com.srinath.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
public class AuthDebugController {

    @GetMapping("/auth-info")
    public String getAuthInfo(Authentication authentication) {
        if (authentication == null) {
            return "No authentication found";
        }
        
        StringBuilder info = new StringBuilder();
        info.append("Authentication: ").append(authentication.getClass().getSimpleName()).append("\n");
        info.append("Principal: ").append(authentication.getPrincipal()).append("\n");
        info.append("Authorities: ").append(authentication.getAuthorities()).append("\n");
        info.append("Name: ").append(authentication.getName()).append("\n");
        
        return info.toString();
    }

    @GetMapping("/user-details")
    public String getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() != null) {
            return "Principal class: " + auth.getPrincipal().getClass().getSimpleName() + 
                   "\nPrincipal: " + auth.getPrincipal() +
                   "\nUser ID: " + (auth.getPrincipal() instanceof com.srinath.attendance.security.CustomUserDetails ? 
                   ((com.srinath.attendance.security.CustomUserDetails) auth.getPrincipal()).getUserId() : "N/A") +
                   "\nAuthorities: " + auth.getAuthorities() +
                   "\nName: " + auth.getName() +
                   "\nAuthenticated: " + auth.isAuthenticated() +
                   "\nAuthorities type: " + (auth.getAuthorities().iterator().next().getClass().getSimpleName());
        }
        return "No authentication";
    }

    @GetMapping("/test-employee")
    public String testEmployeeAccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return "Employee access test successful. User: " + auth.getName() + 
                   ", Authorities: " + auth.getAuthorities();
        }
        return "No authentication";
    }
}
