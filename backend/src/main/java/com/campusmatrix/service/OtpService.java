package com.campusmatrix.service;

import com.campusmatrix.model.User;
import com.campusmatrix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    
    private final UserRepository userRepository;
    private final EmailService emailService; // You'll need to implement this
    
    // In-memory OTP storage (use Redis in production)
    private final Map<String, OtpData> otpStore = new HashMap<>();
    
    @Value("${otp.expiration.minutes:5}")
    private int otpExpirationMinutes;
    
    public String generateOtp(String email) {
        // Check if user already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }
        
        // Generate 6-digit OTP
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(999999));
        
        // Store OTP with expiration
        OtpData otpData = new OtpData(otp, LocalDateTime.now().plusMinutes(otpExpirationMinutes));
        otpStore.put(email, otpData);
        
        // Send OTP via email (implement EmailService)
        // emailService.sendOtpEmail(email, otp);
        
        // For development, log OTP
        System.out.println("OTP for " + email + ": " + otp);
        
        return otp;
    }
    
    public boolean verifyOtp(String email, String otp) {
        OtpData otpData = otpStore.get(email);
        
        if (otpData == null) {
            return false;
        }
        
        if (otpData.expiryTime.isBefore(LocalDateTime.now())) {
            otpStore.remove(email); // Clean up expired OTP
            return false;
        }
        
        if (otpData.otp.equals(otp)) {
            otpStore.remove(email); // Remove OTP after successful verification
            return true;
        }
        
        return false;
    }
    
    public void resendOtp(String email) {
        // Remove existing OTP if any
        otpStore.remove(email);
        
        // Generate new OTP
        generateOtp(email);
    }
    
    private static class OtpData {
        String otp;
        LocalDateTime expiryTime;
        
        OtpData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
    }
}