package com.srinath.attendance.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6,max = 100, message = "Password must be at least 6 characters long")
    private String password;
}
