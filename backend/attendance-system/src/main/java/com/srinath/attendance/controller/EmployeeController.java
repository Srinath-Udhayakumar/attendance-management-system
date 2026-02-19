package com.srinath.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    // 🔹 Quick protected test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test(Authentication authentication) {
        return ResponseEntity.ok(
                "Employee access granted to: " + authentication.getName()
        );
    }
}
