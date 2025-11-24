package com.lms.lms_backend.service;

import java.util.List;

import com.lms.lms_backend.dto.QuestionDTO;
import com.lms.lms_backend.dto.QuizDTO;
import com.lms.lms_backend.dto.QuizSubmissionDTO;

public interface QuizService {
    QuizDTO getQuizByLessonId(Long lessonId);
    QuizDTO submitQuiz(Long userId, QuizSubmissionDTO submission);
    List<QuizDTO> getUserQuizHistory(Long userId);
    List<QuestionDTO> getQuizQuestions(Long quizId);
    QuizDTO createQuiz(Long lessonId, QuizDTO quizDTO);
    QuestionDTO addQuestionToQuiz(Long quizId, QuestionDTO questionDTO);
}