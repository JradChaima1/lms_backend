package com.lms.lms_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.lms_backend.dto.CourseDTO;
import com.lms.lms_backend.dto.ProgressDTO;
import com.lms.lms_backend.dto.UpdateProfileDTO;
import com.lms.lms_backend.dto.UserDTO;
import com.lms.lms_backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")

@Tag(name = "User Management", description = "APIs for user operations and progress tracking")
public class UserController {

    @Autowired
    private UserService userService;

    // ==================== CURRENT USER ENDPOINTS (SECURE) ====================
    
    @GetMapping("/me")
    @Operation(
        summary = "Get current user profile", 
        description = "Retrieve profile information for the currently authenticated user",
        security = @SecurityRequirement(name = "bearerAuth")  // ← ADD THIS LINE
    )
    public ResponseEntity<UserDTO> getCurrentUser() {
        UserDTO user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me/progress")
    @Operation(
        summary = "Get my progress", 
        description = "Retrieve learning progress for the currently authenticated user",
        security = @SecurityRequirement(name = "bearerAuth")  // ← ADD THIS LINE
    )
    public ResponseEntity<List<ProgressDTO>> getMyProgress() {
        List<ProgressDTO> progress = userService.getMyProgress();
        return ResponseEntity.ok(progress);
    }
    @PostMapping("/me/enrollments/{courseId}")
    @Operation(
    summary = "Enroll in course", 
    description = "Enroll the currently authenticated user in a specific course",
    security = @SecurityRequirement(name = "bearerAuth")
)
public ResponseEntity<Void> enrollInCourse(@PathVariable Long courseId) {
    userService.enrollInCourse(courseId);
    return ResponseEntity.ok().build();
}

@GetMapping("/me/courses")
@Operation(
    summary = "Get my enrolled courses", 
    description = "Retrieve all courses the currently authenticated user is enrolled in",
    security = @SecurityRequirement(name = "bearerAuth")
)
public ResponseEntity<List<CourseDTO>> getMyEnrolledCourses() {
    List<CourseDTO> courses = userService.getMyEnrolledCourses();
    return ResponseEntity.ok(courses);
}


    // ==================== ADMIN ENDPOINTS (KEPT FOR BACKWARDS COMPATIBILITY) ====================
    
    @GetMapping("/{userId}")
    @Operation(
        summary = "Get user by ID [ADMIN]", 
        description = "Retrieve user profile information by user ID (Admin functionality)",
        security = @SecurityRequirement(name = "bearerAuth")  // ← ADD THIS LINE (optional for admin)
    )
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/progress")
    @Operation(
        summary = "Get user progress by ID [ADMIN]", 
        description = "Retrieve progress for a specific user across all enrolled courses (Admin functionality)",
        security = @SecurityRequirement(name = "bearerAuth")  // ← ADD THIS LINE (optional for admin)
    )
    public ResponseEntity<List<ProgressDTO>> getUserProgress(@PathVariable Long userId) {
        List<ProgressDTO> progress = userService.getUserProgress(userId);
        return ResponseEntity.ok(progress);
    }
    
    @PostMapping("/{userId}/enrollments/{courseId}")
    @Operation(
        summary = "Enroll user in course [ADMIN]", 
        description = "Enroll a specific user in a course (Admin functionality)",
        security = @SecurityRequirement(name = "bearerAuth")  // ← ADD THIS LINE (optional for admin)
    )
    public ResponseEntity<Void> enrollUserInCourse(
            @PathVariable Long userId, 
            @PathVariable Long courseId) {
        userService.enrollInCourse(userId, courseId);
        return ResponseEntity.ok().build();
    }




    @PutMapping("/me")
    @Operation(
    summary = "Update my profile",
    description = "Update profile information for the currently authenticated user",
    security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<UserDTO> updateMyProfile(@RequestBody UpdateProfileDTO updateProfileDTO) {
    UserDTO user = userService.updateMyProfile(updateProfileDTO);
    return ResponseEntity.ok(user);
   }

   @PutMapping("/{userId}")
   @Operation(
    summary = "Update user profile [ADMIN]",
    description = "Update profile information for a specific user (Admin functionality)",
    security = @SecurityRequirement(name = "bearerAuth")
   )
    public ResponseEntity<UserDTO> updateUserProfile(
        @PathVariable Long userId,
        @RequestBody UpdateProfileDTO updateProfileDTO) {
    UserDTO user = userService.updateProfile(userId, updateProfileDTO);
    return ResponseEntity.ok(user);
}

}