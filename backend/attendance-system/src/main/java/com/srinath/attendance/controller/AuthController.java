package com.srinath.attendance.controller;

import com.srinath.attendance.dto.request.LoginRequest;
import com.srinath.attendance.dto.request.RegisterRequest;
import com.srinath.attendance.dto.response.AuthResponse;
import com.srinath.attendance.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 🔐 REGISTER
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // 🔐 LOGIN
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
