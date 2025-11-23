package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.UserDTO;
import com.lms.lms_backend.dto.ProgressDTO;
import com.lms.lms_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for user operations and progress tracking")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieve user profile information")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/progress")
    @Operation(summary = "Get user progress", description = "Retrieve progress across all enrolled courses")
    public ResponseEntity<List<ProgressDTO>> getUserProgress(@PathVariable Long userId) {
        List<ProgressDTO> progress = userService.getUserProgress(userId);
        return ResponseEntity.ok(progress);
    }

    @PostMapping("/{userId}/enrollments/{courseId}")
    @Operation(summary = "Enroll in course", description = "Enroll user in a specific course")
    public ResponseEntity<Void> enrollInCourse(
            @PathVariable Long userId, 
            @PathVariable Long courseId) {
        
        userService.enrollInCourse(userId, courseId);
        return ResponseEntity.ok().build();
    }
}