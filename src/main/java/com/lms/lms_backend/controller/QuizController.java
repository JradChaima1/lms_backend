package com.lms.lms_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.lms_backend.dto.QuestionDTO;
import com.lms.lms_backend.dto.QuizDTO;
import com.lms.lms_backend.dto.QuizResultDTO;
import com.lms.lms_backend.dto.QuizSubmissionDTO;
import com.lms.lms_backend.service.QuizService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/quizzes")
@Tag(name = "Quiz Management", description = "APIs for quiz operations and submissions")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/lesson/{lessonId}")
    @Operation(summary = "Get quiz by lesson", description = "Retrieve quiz for a specific lesson")
    public ResponseEntity<QuizDTO> getQuizByLessonId(@PathVariable Long lessonId) {
        QuizDTO quiz = quizService.getQuizByLessonId(lessonId);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/{userId}/submit")
    @Operation(summary = "Submit quiz", description = "Submit quiz answers and get results")
    public ResponseEntity<QuizResultDTO> submitQuiz(

            @PathVariable Long userId,
            @RequestBody QuizSubmissionDTO submission) {
        
        QuizResultDTO result = quizService.submitQuiz(userId, submission);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}/history")
    @Operation(summary = "Get quiz history", description = "Retrieve user's quiz attempt history")
    public ResponseEntity<List<QuizDTO>> getUserQuizHistory(@PathVariable Long userId) {
        List<QuizDTO> history = quizService.getUserQuizHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{quizId}/questions")
    @Operation(summary = "Get quiz questions", description = "Retrieve questions for a specific quiz")
    public ResponseEntity<List<QuestionDTO>> getQuizQuestions(@PathVariable Long quizId) {
        List<QuestionDTO> questions = quizService.getQuizQuestions(quizId);
        return ResponseEntity.ok(questions);
    }
}