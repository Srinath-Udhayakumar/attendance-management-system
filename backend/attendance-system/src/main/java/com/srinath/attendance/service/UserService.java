package com.srinath.attendance.service;

import com.srinath.attendance.dto.request.RegisterRequest;
import com.srinath.attendance.entity.User;

import java.util.UUID;

public interface UserService {
    User registerEmployee(RegisterRequest request);
    User registerManager(RegisterRequest request);
    User getUserById(UUID id);
    User getUserByEmail(String email);
}
