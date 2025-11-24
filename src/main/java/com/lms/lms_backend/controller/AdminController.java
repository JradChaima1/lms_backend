package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.CourseDTO;
import com.lms.lms_backend.dto.LessonDTO;
import com.lms.lms_backend.dto.QuizDTO;
import com.lms.lms_backend.dto.QuestionDTO;
import com.lms.lms_backend.service.CourseService;
import com.lms.lms_backend.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Management", description = "APIs for administrative operations")
public class AdminController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private QuizService quizService;

    // ==================== COURSE MANAGEMENT ====================

    @PostMapping("/courses")
    @Operation(
        summary = "Create new course", 
        description = "Create a new course (Admin only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        return ResponseEntity.ok(createdCourse);
    }

    @PutMapping("/courses/{courseId}")
    @Operation(
        summary = "Update course", 
        description = "Update an existing course (Admin only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long courseId, @RequestBody CourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(courseId, courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/courses/{courseId}")
    @Operation(
        summary = "Delete course", 
        description = "Delete a course (Admin only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok().build();
    }

    // ==================== LESSON MANAGEMENT ====================

    @PostMapping("/courses/{courseId}/lessons")
    @Operation(
        summary = "Create lesson", 
        description = "Create a new lesson for a course (Admin only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<LessonDTO> createLesson(@PathVariable Long courseId, @RequestBody LessonDTO lessonDTO) {
        LessonDTO createdLesson = courseService.createLesson(courseId, lessonDTO);
        return ResponseEntity.ok(createdLesson);
    }

    @PutMapping("/lessons/{lessonId}")
    @Operation(
        summary = "Update lesson", 
        description = "Update an existing lesson (Admin only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<LessonDTO> updateLesson(@PathVariable Long lessonId, @RequestBody LessonDTO lessonDTO) {
        LessonDTO updatedLesson = courseService.updateLesson(lessonId, lessonDTO);
        return ResponseEntity.ok(updatedLesson);
    }

    @DeleteMapping("/lessons/{lessonId}")
    @Operation(
        summary = "Delete lesson", 
        description = "Delete a lesson (Admin only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        courseService.deleteLesson(lessonId);
        return ResponseEntity.ok().build();
    }

    // ==================== QUIZ MANAGEMENT ====================

    @PostMapping("/lessons/{lessonId}/quiz")
    @Operation(
        summary = "Create quiz", 
        description = "Create a quiz for a lesson (Admin only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<QuizDTO> createQuiz(@PathVariable Long lessonId, @RequestBody QuizDTO quizDTO) {
        QuizDTO createdQuiz = quizService.createQuiz(lessonId, quizDTO);
        return ResponseEntity.ok(createdQuiz);
    }

    @PostMapping("/quizzes/{quizId}/questions")
    @Operation(
        summary = "Add question to quiz", 
        description = "Add a question to a quiz (Admin only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<QuestionDTO> addQuestionToQuiz(@PathVariable Long quizId, @RequestBody QuestionDTO questionDTO) {
        QuestionDTO createdQuestion = quizService.addQuestionToQuiz(quizId, questionDTO);
        return ResponseEntity.ok(createdQuestion);
    }
}