package com.srinath.attendance.service;

import com.srinath.attendance.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User createUser(User user);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(UUID id);

    boolean existsByEmail(String email);
}
