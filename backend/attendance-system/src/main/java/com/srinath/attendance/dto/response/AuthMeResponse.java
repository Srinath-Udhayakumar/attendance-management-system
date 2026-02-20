package com.srinath.attendance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthMeResponse {
    private UUID id;
    private String name;
    private String email;
    private String role;
    private String employeeId;
    private String department;
}
