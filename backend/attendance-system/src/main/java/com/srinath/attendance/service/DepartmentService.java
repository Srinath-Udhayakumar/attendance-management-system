package com.srinath.attendance.service;

import com.srinath.attendance.entity.Department;

import java.util.Optional;

public interface DepartmentService {

    Department createDepartment(Department department);

    Optional<Department> findByName(String name);
}
