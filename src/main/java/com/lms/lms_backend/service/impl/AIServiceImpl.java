package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.service.AIService;
import com.lms.lms_backend.entity.Quiz;
import com.lms.lms_backend.repository.QuizRepository;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIServiceImpl implements AIService {

    @Autowired
    private QuizRepository quizRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String AI_API_URL = "https://api-inference.huggingface.co/models/your-model";

    @Override
    public String generateQuizFeedback(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));

        // Calculate performance metrics
        double scorePercentage = (double) quiz.getScore() / quiz.getTotalQuestions() * 100;
        
        // Generate personalized feedback based on performance
        String performanceLevel;
        if (scorePercentage >= 80) {
            performanceLevel = "excellent";
        } else if (scorePercentage >= 60) {
            performanceLevel = "good";
        } else {
            performanceLevel = "needs improvement";
        }

        // In a real implementation, you'd call an AI API here
        // For now, return template-based feedback
        return String.format(
            "You scored %d out of %d (%.1f%%). This is %s. Focus on practicing the concepts from this lesson to improve your understanding.",
            quiz.getScore(), quiz.getTotalQuestions(), scorePercentage, performanceLevel
        );
    }

    @Override
    public String getPersonalizedLearningRecommendation(Long userId) {
        // Analyze user's quiz history and progress
        // Recommend next steps based on performance
        
        // Mock implementation - in real scenario, call AI API
        return "Based on your progress, we recommend focusing on advanced topics in your current course. Consider reviewing the previous lesson to strengthen your foundation.";
    }
}