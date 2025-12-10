package com.campusmatrix.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class RegisterRequest {
    
    // Keep existing backend field names but update mapping
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @NotBlank(message = "Student ID is required")
    private String studentId;
    
    @NotBlank(message = "College email is required")
    @Email(message = "Invalid college email format")
    private String collegeEmail;  // Changed from 'email' to 'collegeEmail'
    
    @Email(message = "Invalid personal email format")
    private String personalEmail; // Add this new field
    
    @NotBlank(message = "Academic year is required")
    private String year;  // Changed from 'yearOfStudy' to 'year'
    
    @NotBlank(message = "Department is required")
    private String department;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    private String phoneNumber; // Optional, add field to your form or keep optional
}