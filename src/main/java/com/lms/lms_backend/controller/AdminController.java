package com.lms.lms_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.lms_backend.dto.CourseDTO;
import com.lms.lms_backend.dto.LessonDTO;
import com.lms.lms_backend.dto.QuestionDTO;
import com.lms.lms_backend.dto.QuizDTO;
import com.lms.lms_backend.dto.UserDTO;
import com.lms.lms_backend.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Management", description = "APIs for admin operations")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/stats")
    @Operation(
        summary = "Get system statistics",
        description = "Retrieve overall system statistics",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = adminService.getSystemStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users")
    @Operation(
        summary = "Get all users",
        description = "Retrieve list of all users",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/{userId}")
    @Operation(
        summary = "Delete user",
        description = "Delete a user by ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/courses")
    @Operation(
        summary = "Get all courses",
        description = "Retrieve list of all courses",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = adminService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/courses")
    @Operation(
        summary = "Create course",
        description = "Create a new course",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        System.out.println("Received CourseDTO: " + courseDTO.getTitle());
        System.out.println("ImageURL: " + courseDTO.getImageUrl());
        CourseDTO course = adminService.createCourse(courseDTO);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/courses/{courseId}")
    @Operation(
        summary = "Update course",
        description = "Update an existing course",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CourseDTO> updateCourse(
            @PathVariable Long courseId,
            @RequestBody CourseDTO courseDTO) {
        CourseDTO course = adminService.updateCourse(courseId, courseDTO);
        return ResponseEntity.ok(course);
    }

    @DeleteMapping("/courses/{courseId}")
    @Operation(
        summary = "Delete course",
        description = "Delete a course by ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        adminService.deleteCourse(courseId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/courses/{courseId}/lessons")
    @Operation(
        summary = "Create lesson",
        description = "Create a new lesson for a course",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<LessonDTO> createLesson(
            @PathVariable Long courseId,
            @RequestBody LessonDTO lessonDTO) {
        LessonDTO lesson = adminService.createLesson(courseId, lessonDTO);
        return ResponseEntity.ok(lesson);
    }

    @PutMapping("/lessons/{lessonId}")
    @Operation(
        summary = "Update lesson",
        description = "Update an existing lesson",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<LessonDTO> updateLesson(
            @PathVariable Long lessonId,
            @RequestBody LessonDTO lessonDTO) {
        LessonDTO lesson = adminService.updateLesson(lessonId, lessonDTO);
        return ResponseEntity.ok(lesson);
    }

    @DeleteMapping("/lessons/{lessonId}")
    @Operation(
        summary = "Delete lesson",
        description = "Delete a lesson by ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        adminService.deleteLesson(lessonId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lessons/{lessonId}/quiz")
    @Operation(
        summary = "Create quiz",
        description = "Create a quiz for a lesson",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<QuizDTO> createQuiz(
            @PathVariable Long lessonId,
            @RequestBody QuizDTO quizDTO) {
        QuizDTO quiz = adminService.createQuiz(lessonId, quizDTO);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/quizzes/{quizId}/questions")
    @Operation(
        summary = "Add question",
        description = "Add a question to a quiz",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<QuestionDTO> addQuestion(
            @PathVariable Long quizId,
            @RequestBody QuestionDTO questionDTO) {
        QuestionDTO question = adminService.addQuestion(quizId, questionDTO);
        return ResponseEntity.ok(question);
    }

    @DeleteMapping("/questions/{questionId}")
    @Operation(
        summary = "Delete question",
        description = "Delete a question by ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        adminService.deleteQuestion(questionId);
        return ResponseEntity.ok().build();
    }
}
