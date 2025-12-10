package com.campusmatrix.model;

import lombok.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "password_hash", nullable = false)
    private String password;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(name = "department")
    private String department;
    
    @Column(name = "year_of_study")
    private String yearOfStudy;
    
    @Column(name = "student_id", unique = true)
    private String studentId;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Remove role field entirely
}