package com.srinath.attendance.service.impl;

import com.srinath.attendance.entity.Role;
import com.srinath.attendance.entity.RoleType;
import com.srinath.attendance.repository.RoleRepository;
import com.srinath.attendance.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> findByName(RoleType name) {
        return roleRepository.findByName(name);
    }

}
