package com.lms.lms_backend.service;

public interface AIService {
    String generateQuizFeedback(Long quizId);
    String getPersonalizedLearningRecommendation(Long userId);
}