package com.lms.lms_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.lms_backend.dto.LoginRequest;
import com.lms.lms_backend.dto.RegistrationRequest;
import com.lms.lms_backend.dto.UserDTO;
import com.lms.lms_backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new student account")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        UserDTO registeredUser = userService.registerUser(registrationRequest);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return user details")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserDTO user = userService.login(loginRequest);
        return ResponseEntity.ok(user);
    }
}