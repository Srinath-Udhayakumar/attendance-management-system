package com.srinath.attendance.service;

import com.srinath.attendance.entity.Department;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentService {

    Department createDepartment(Department department);

    Optional<Department> findByName(String name);

    Optional<Department> findDepartmentById(UUID id);
}
