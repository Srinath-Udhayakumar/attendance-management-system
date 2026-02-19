package com.srinath.attendance.service.impl;

import com.srinath.attendance.dto.request.LoginRequest;
import com.srinath.attendance.dto.request.RegisterRequest;
import com.srinath.attendance.dto.response.AuthResponse;
import com.srinath.attendance.entity.Department;
import com.srinath.attendance.entity.Role;
import com.srinath.attendance.entity.RoleType;
import com.srinath.attendance.entity.User;
import com.srinath.attendance.security.JwtService;
import com.srinath.attendance.service.AuthService;
import com.srinath.attendance.service.DepartmentService;
import com.srinath.attendance.service.RoleService;
import com.srinath.attendance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RoleService roleService;
    private final DepartmentService departmentService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userService.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new RuntimeException("Email already exists");
        }

        Role role = roleService.findByName(RoleType.EMPLOYEE)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Department department = departmentService.findByName("IT")
                .orElseThrow(() -> new RuntimeException("Department not found"));

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .department(department)
                .enabled(true)
                .build();

        userService.createUser(user);

        String token = jwtService.generateToken(user.getEmail(), user.getRole().getName().name());

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase().trim(),
                        request.getPassword()
                )
        );

        User user = userService.getUserByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow();

        String token = jwtService.generateToken(user.getEmail(), user.getRole().getName().name());

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
