package com.srinath.attendance.dto.request;

import com.srinath.attendance.entity.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Email
    @NotBlank
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(min = 6,max = 100, message = "Password must be at least 6 characters")
    private String password;

    @NotNull
    private UUID departmentId;

    private RoleType role;
}
