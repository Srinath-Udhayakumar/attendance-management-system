package com.srinath.attendance.repository;

import com.srinath.attendance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
//@Query("""
//    SELECT u FROM User u
//    LEFT JOIN FETCH u.role
//    LEFT JOIN FETCH u.department
//    WHERE LOWER(u.email) = LOWER(:email)
//""")
//Optional<User> findByEmail(@Param("email") String email);