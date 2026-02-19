package com.srinath.attendance.service.impl;

import com.srinath.attendance.entity.Department;
import com.srinath.attendance.repository.DepartmentRepository;
import com.srinath.attendance.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Optional<Department> findByName(String name) {
        return departmentRepository.findByName(name);
    }
}
