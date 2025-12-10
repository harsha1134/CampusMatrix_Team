package com.campusmatrix.controller;

import com.campusmatrix.dto.OtpRequest;
import com.campusmatrix.dto.OtpVerificationRequest;
import com.campusmatrix.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {
    
    private final OtpService otpService;
    
    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody OtpRequest otpRequest) {
        try {
            String otp = otpService.generateOtp(otpRequest.getEmail());
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "OTP sent successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request) {
        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        
        if (isValid) {
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "OTP verified successfully"
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Invalid or expired OTP"
            ));
        }
    }
    
    @PostMapping("/resend")
    public ResponseEntity<?> resendOtp(@RequestBody OtpRequest otpRequest) {
        try {
            otpService.resendOtp(otpRequest.getEmail());
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "OTP resent successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}