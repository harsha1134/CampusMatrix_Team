package com.campusmatrix.dto;

import lombok.Data;

@Data
public class OtpRequest {
    private String email;
}

@Data
public class OtpVerificationRequest {
    private String email;
    private String otp;
}