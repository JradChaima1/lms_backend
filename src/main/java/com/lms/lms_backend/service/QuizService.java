package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.QuizDTO;
import com.lms.lms_backend.dto.QuestionDTO;
import com.lms.lms_backend.dto.QuizSubmissionDTO;
import java.util.List;

public interface QuizService {
    QuizDTO getQuizByLessonId(Long lessonId);
    QuizDTO submitQuiz(Long userId, QuizSubmissionDTO submission);
    List<QuizDTO> getUserQuizHistory(Long userId);
    List<QuestionDTO> getQuizQuestions(Long quizId);
}