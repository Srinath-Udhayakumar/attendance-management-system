package com.srinath.attendance.service.impl;

import com.srinath.attendance.dto.request.LoginRequest;
import com.srinath.attendance.dto.request.RegisterRequest;
import com.srinath.attendance.dto.response.AuthMeResponse;
import com.srinath.attendance.dto.response.AuthResponse;
import com.srinath.attendance.entity.Department;
import com.srinath.attendance.entity.Role;
import com.srinath.attendance.entity.User;
import com.srinath.attendance.exception.DepartmentNotFoundException;
import com.srinath.attendance.exception.ResourceAlreadyExistsException;
import com.srinath.attendance.exception.RoleNotFoundException;
import com.srinath.attendance.security.JwtService;
import com.srinath.attendance.service.AuthService;
import com.srinath.attendance.service.DepartmentService;
import com.srinath.attendance.repository.UserRepository;
import com.srinath.attendance.service.RoleService;
import com.srinath.attendance.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RoleService roleService;
    private final DepartmentService departmentService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        String normalizedEmail = request.getEmail().toLowerCase().trim();

        if (userService.existsByEmail(normalizedEmail)) {
            log.warn("Registration failed: Email already registered - {}", normalizedEmail);
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        Role role = roleService.findByName(request.getRole())
                .orElseThrow(() -> {
                    log.error("Role not found during registration: {}",request.getRole());
                    return new RoleNotFoundException("Role not found");
                });

        Department department = departmentService.findDepartmentById(request.getDepartmentId())
                .orElseThrow(() -> {
                    log.error("Department not found with id: {}", request.getDepartmentId());
                    return new DepartmentNotFoundException("Department not found");
                });

        // Generate unique employee ID
        String employeeId = "EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        User user = User.builder()
                .name(request.getName())
                .email(normalizedEmail)
                .password(passwordEncoder.encode(request.getPassword()))
                .employeeId(employeeId)
                .role(role)
                .department(department)
                .enabled(true)
                .build();

        userService.createUser(user);
        log.info("User registered successfully with employee ID: {}", employeeId);

        String token = jwtService.generateToken(user.getEmail(), user.getRole().getName().name());

        return AuthResponse.builder()
                .token(token).tokenType("Bearer")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getEmail());

        String normalizedEmail = request.getEmail().toLowerCase().trim();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            normalizedEmail,
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            log.warn("Login failed for user: {}", normalizedEmail);
            throw e;
        }

        User user = userService.getUserByEmail(normalizedEmail)
                .orElseThrow(() -> {
                    log.error("User not found after authentication: {}", normalizedEmail);
                    return new RuntimeException("User not found");
                });

        log.info("User logged in successfully: {}", normalizedEmail);
        String token = jwtService.generateToken(user.getEmail(), user.getRole().getName().name());

        return AuthResponse.builder()
                .token(token).tokenType("Bearer")
                .build();
    }

    @Override
    public AuthMeResponse getCurrentUserDetails(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return AuthMeResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .employeeId(user.getEmployeeId())
                .department(user.getDepartment() != null ? user.getDepartment().getName() : "")
                .build();
    }
}
