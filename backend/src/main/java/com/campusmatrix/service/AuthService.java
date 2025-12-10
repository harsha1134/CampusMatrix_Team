package com.campusmatrix.service;

import com.campusmatrix.dto.AuthResponse;
import com.campusmatrix.dto.LoginRequest;
import com.campusmatrix.dto.RegisterRequest;
import com.campusmatrix.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtService.generateToken(userDetails);
        
        UserDTO userDTO = userService.getUserByEmail(loginRequest.getEmail());
        
        return AuthResponse.builder()
                .token(jwt)
                .user(userDTO)
                .message("Login successful")
                .build();
    }
    
    public AuthResponse register(RegisterRequest registerRequest) {
        UserDTO userDTO = userService.registerUser(registerRequest);
        
        // Auto-login after registration
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(registerRequest.getCollegeEmail());
        loginRequest.setPassword(registerRequest.getPassword());
        
        return login(loginRequest);
    }
}