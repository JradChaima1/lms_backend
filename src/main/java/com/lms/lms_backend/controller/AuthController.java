package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.UserDTO;
import com.lms.lms_backend.dto.LoginRequest;
import com.lms.lms_backend.dto.RegistrationRequest;
import com.lms.lms_backend.service.UserService;
import com.lms.lms_backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new student account")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        UserDTO registeredUser = userService.registerUser(registrationRequest);
        
        String token = jwtUtil.generateToken(registeredUser.getEmail());
        AuthResponse response = new AuthResponse(token, registeredUser);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserDTO user = userService.login(loginRequest);
        
        String token = jwtUtil.generateToken(user.getEmail());
        AuthResponse response = new AuthResponse(token, user);
        
        return ResponseEntity.ok(response);
    }

    
    public static class AuthResponse {
        private String token;
        private String type = "Bearer";
        private UserDTO user;

        public AuthResponse(String token, UserDTO user) {
            this.token = token;
            this.user = user;
        }

 
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public UserDTO getUser() { return user; }
        public void setUser(UserDTO user) { this.user = user; }
    }
}