package com.srinath.attendance.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private String role;
    private String department;
}
