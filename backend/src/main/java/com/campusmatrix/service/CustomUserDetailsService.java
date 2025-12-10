package com.campusmatrix.service;

import com.campusmatrix.model.User;
import com.campusmatrix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        // Since role column is removed, hardcode as STUDENT or get from another source
        String role = "STUDENT"; // Default all users to STUDENT
        
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),  // Make sure this matches your entity field name
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}