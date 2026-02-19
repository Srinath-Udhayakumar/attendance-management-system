package com.srinath.attendance.service;

import com.srinath.attendance.entity.Role;
import com.srinath.attendance.entity.RoleType;

import java.util.Optional;

public interface RoleService {

    Role createRole(Role role);

    Optional<Role> findByName(RoleType name);

}
