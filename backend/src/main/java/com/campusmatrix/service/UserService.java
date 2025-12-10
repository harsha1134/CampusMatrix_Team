package com.campusmatrix.service;

import com.campusmatrix.dto.RegisterRequest;
import com.campusmatrix.dto.UserDTO;
import com.campusmatrix.model.User;
import com.campusmatrix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    
    @Transactional
public UserDTO registerUser(RegisterRequest registerRequest) {
    // Check if user already exists using collegeEmail
    if (userRepository.existsByEmail(registerRequest.getCollegeEmail())) {
        throw new RuntimeException("College email already registered");
    }
    
    if (registerRequest.getStudentId() != null && 
        userRepository.existsByStudentId(registerRequest.getStudentId())) {
        throw new RuntimeException("Student ID already registered");
    }
    
    // Create new user - map all fields correctly
    User user = new User();
    user.setEmail(registerRequest.getCollegeEmail()); // Map collegeEmail to email field
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    user.setFullName(registerRequest.getFullName());
    user.setStudentId(registerRequest.getStudentId());
    user.setDepartment(registerRequest.getDepartment());
    user.setYearOfStudy(registerRequest.getYear()); // Map year to yearOfStudy
    user.setPhoneNumber(registerRequest.getPhoneNumber()); // Optional
    
    // Store personalEmail in another field if you have it, or add to User entity
    // user.setPersonalEmail(registerRequest.getPersonalEmail());
    
    user.setIsActive(true);
    user.setIsEmailVerified(false);
    
    User savedUser = userRepository.save(user);
    return convertToDTO(savedUser);
}
    
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }
    
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }
    
    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
    
    public User getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}