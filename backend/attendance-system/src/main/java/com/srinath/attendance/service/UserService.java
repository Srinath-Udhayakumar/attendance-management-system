package com.srinath.attendance.service;

import com.srinath.attendance.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User createUser(User user);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(UUID id);

    boolean existsByEmail(String email);

    List<User> getAllUsers();

    List<User> getUsersByDepartment(UUID departmentId);

    Page<User> getAllUsersPaginated(Pageable pageable);

    User updateUser(User user);

    void deleteUser(UUID id);
}
