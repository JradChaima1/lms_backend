package com.lms.lms_backend.controller;

import com.lms.lms_backend.service.AIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI Services", description = "APIs for AI-generated feedback and recommendations")
public class AIController {

    @Autowired
    private AIService aiService;

    @GetMapping("/quizzes/{quizId}/feedback")
    @Operation(summary = "Get AI feedback", description = "Generate AI-powered feedback for quiz performance")
    public ResponseEntity<Map<String, String>> getQuizFeedback(@PathVariable Long quizId) {
        String feedback = aiService.generateQuizFeedback(quizId);
        return ResponseEntity.ok(Map.of("feedback", feedback));
    }

    @GetMapping("/users/{userId}/recommendations")
    @Operation(summary = "Get learning recommendations", description = "Get personalized learning recommendations based on progress")
    public ResponseEntity<Map<String, String>> getLearningRecommendations(@PathVariable Long userId) {
        String recommendations = aiService.getPersonalizedLearningRecommendation(userId);
        return ResponseEntity.ok(Map.of("recommendations", recommendations));
    }
}