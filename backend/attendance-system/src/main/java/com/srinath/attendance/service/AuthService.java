package com.srinath.attendance.service;

import com.srinath.attendance.dto.request.LoginRequest;
import com.srinath.attendance.dto.request.RegisterRequest;
import com.srinath.attendance.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
