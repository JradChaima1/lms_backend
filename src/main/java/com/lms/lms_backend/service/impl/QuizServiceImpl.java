package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.QuizDTO;
import com.lms.lms_backend.dto.QuestionDTO;
import com.lms.lms_backend.dto.QuizSubmissionDTO;
import com.lms.lms_backend.entity.*;
import com.lms.lms_backend.repository.*;
import com.lms.lms_backend.service.QuizService;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public QuizDTO getQuizByLessonId(Long lessonId) {
        Quiz quiz = quizRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found for lesson id: " + lessonId));
        
        return convertToDTO(quiz);
    }

    @Override
    public QuizDTO submitQuiz(Long userId, QuizSubmissionDTO submission) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Lesson lesson = lessonRepository.findById(submission.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + submission.getLessonId()));

        // Get the quiz for this lesson
        Quiz quiz = quizRepository.findByLessonId(lesson.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found for lesson id: " + lesson.getId()));

        // Create a new quiz attempt
        Quiz quizAttempt = new Quiz();
        quizAttempt.setTitle(quiz.getTitle());
        quizAttempt.setUser(user);
        quizAttempt.setLesson(lesson);
        quizAttempt.setTotalQuestions(quiz.getQuestions().size());
        quizAttempt.setAttemptedAt(LocalDateTime.now());

        // Calculate score
        int correctAnswers = calculateScore(quiz, submission);
        quizAttempt.setScore(correctAnswers);

        // Save quiz attempt
        Quiz savedQuiz = quizRepository.save(quizAttempt);

        // Update enrollment progress
        updateUserProgress(userId, lesson.getCourse().getId());

        return convertToDTO(savedQuiz);
    }

    @Override
    public List<QuizDTO> getUserQuizHistory(Long userId) {
        return quizRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionDTO> getQuizQuestions(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));
        
        return quiz.getQuestions().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private int calculateScore(Quiz quiz, QuizSubmissionDTO submission) {
        int correctCount = 0;

        for (QuizSubmissionDTO.QuestionAnswerDTO answer : submission.getAnswers()) {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + answer.getQuestionId()));

            // Check if answer is correct
            if (question.getCorrectAnswer().equals(answer.getSelectedAnswer())) {
                correctCount++;
            }
        }

        return correctCount;
    }

    private void updateUserProgress(Long userId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for user and course"));

        // Calculate new progress based on completed quizzes
        Course course = enrollment.getCourse();
        long totalQuizzes = course.getLessons().stream()
                .filter(lesson -> lesson.getQuiz() != null)
                .count();
        
        long completedQuizzes = quizRepository.findByUserIdAndLessonId(userId, null).stream()
                .filter(quiz -> course.getLessons().contains(quiz.getLesson()))
                .count();

        double progress = totalQuizzes > 0 ? (double) completedQuizzes / totalQuizzes : 0;
        enrollment.setProgress(progress);
        enrollment.setCompleted(progress >= 1.0);

        enrollmentRepository.save(enrollment);
    }

    private QuizDTO convertToDTO(Quiz quiz) {
        QuizDTO dto = new QuizDTO();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setAttemptedAt(quiz.getAttemptedAt());
        dto.setScore(quiz.getScore());
        dto.setTotalQuestions(quiz.getTotalQuestions());
        dto.setUserId(quiz.getUser().getId());
        dto.setLessonId(quiz.getLesson().getId());
        return dto;
    }

    private QuestionDTO convertToDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());
        dto.setOptionA(question.getOptionA());
        dto.setOptionB(question.getOptionB());
        dto.setOptionC(question.getOptionC());
        dto.setOptionD(question.getOptionD());
        return dto;
    }
}