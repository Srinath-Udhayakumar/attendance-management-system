package com.srinath.attendance.repository;

import com.srinath.attendance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.department.id = :departmentId")
    List<User> findByDepartmentId(@Param("departmentId") UUID departmentId);

    @Query("SELECT u FROM User u WHERE u.role.name = 'MANAGER'")
    List<User> findAllManagers();

    @Query("SELECT u FROM User u WHERE u.role.name = 'EMPLOYEE'")
    List<User> findAllEmployees();
}
