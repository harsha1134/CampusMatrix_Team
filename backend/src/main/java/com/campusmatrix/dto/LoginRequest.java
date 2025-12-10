package com.campusmatrix.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
}