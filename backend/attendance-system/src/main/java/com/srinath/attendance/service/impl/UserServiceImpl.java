package com.srinath.attendance.service.impl;

import com.srinath.attendance.entity.User;
import com.srinath.attendance.repository.UserRepository;
import com.srinath.attendance.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        log.info("Creating new user with email: {}", user.getEmail());
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByDepartment(UUID departmentId) {
        log.info("Fetching users by department: {}", departmentId);
        return userRepository.findByDepartmentId(departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllUsersPaginated(Pageable pageable) {
        log.info("Fetching users with pagination");
        return userRepository.findAll(pageable);
    }

    @Override
    public User updateUser(User user) {
        log.info("Updating user: {}", user.getId());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
        log.info("Deleting user: {}", id);
        userRepository.deleteById(id);
    }
}
