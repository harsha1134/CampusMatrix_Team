package com.campusmatrix.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private UserDTO user;
    private String message;
}