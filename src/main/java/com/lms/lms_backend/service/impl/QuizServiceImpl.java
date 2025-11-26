package com.lms.lms_backend.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.lms_backend.dto.QuestionDTO;
import com.lms.lms_backend.dto.QuestionResultDTO;
import com.lms.lms_backend.dto.QuizDTO;
import com.lms.lms_backend.dto.QuizResultDTO;
import com.lms.lms_backend.dto.QuizSubmissionDTO;
import com.lms.lms_backend.dto.UserDTO;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.Enrollment;
import com.lms.lms_backend.entity.Lesson;
import com.lms.lms_backend.entity.Question;
import com.lms.lms_backend.entity.Quiz;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.repository.EnrollmentRepository;
import com.lms.lms_backend.repository.LessonRepository;
import com.lms.lms_backend.repository.QuestionRepository;
import com.lms.lms_backend.repository.QuizRepository;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.service.AchievementService;
import com.lms.lms_backend.service.QuizService;
import com.lms.lms_backend.service.UserService;
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
    @Autowired
    private UserService userService; 
    @Autowired
    private AchievementService achievementService;

    @Override
    public QuizDTO getQuizByLessonId(Long lessonId) {
        Quiz quiz = quizRepository.findFirstByLessonId(lessonId)

                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found for lesson id: " + lessonId));
        
        return convertToDTO(quiz);
    }
    @Override
public QuizResultDTO submitQuiz(Long userId, QuizSubmissionDTO submission) {
    UserDTO currentUser = userService.getCurrentUser();
    if (!currentUser.getRole().equals("ADMIN") && !currentUser.getId().equals(userId)) {
        throw new RuntimeException("Access denied: You can only submit quizzes for yourself");
    }
    
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    
    Lesson lesson = lessonRepository.findById(submission.getLessonId())
            .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + submission.getLessonId()));

    Course course = lesson.getCourse();
    boolean isEnrolled = enrollmentRepository.findByUserIdAndCourseId(userId, course.getId()).isPresent();
    if (!isEnrolled && !currentUser.getRole().equals("ADMIN")) {
        throw new RuntimeException("Access denied: You must be enrolled in the course to take quizzes");
    }

    Quiz quiz = quizRepository.findFirstByLessonId(lesson.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Quiz not found for lesson id: " + lesson.getId()));

  
    List<QuestionResultDTO> questionResults = new ArrayList<>();
    int correctCount = 0;
    
    for (QuizSubmissionDTO.QuestionAnswerDTO answer : submission.getAnswers()) {
        Question question = questionRepository.findById(answer.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + answer.getQuestionId()));
        
        boolean isCorrect = question.getCorrectAnswer().equals(answer.getSelectedAnswer());
        if (isCorrect) {
            correctCount++;
        }
        
        
        int userAnswerIndex = answer.getSelectedAnswer().charAt(0) - 'A';
        int correctAnswerIndex = question.getCorrectAnswer().charAt(0) - 'A';
        
        QuestionResultDTO questionResult = new QuestionResultDTO();
        questionResult.setQuestionId(question.getId());
        questionResult.setQuestionText(question.getQuestionText());
        questionResult.setUserAnswer(userAnswerIndex);
        questionResult.setCorrectAnswer(correctAnswerIndex);
        questionResult.setIsCorrect(isCorrect);
        
        questionResults.add(questionResult);
    }
    
    int totalQuestions = quiz.getQuestions().size();
    int scorePercentage = (correctCount * 100) / totalQuestions;
    boolean passed = scorePercentage >= 70; // 70% passing score
    
  
    Quiz quizAttempt = new Quiz();
    quizAttempt.setTitle(quiz.getTitle());
    quizAttempt.setUser(user);
    quizAttempt.setLesson(lesson);
    quizAttempt.setTotalQuestions(totalQuestions);
    quizAttempt.setScore(correctCount);
    quizAttempt.setAttemptedAt(LocalDateTime.now());
    quizRepository.save(quizAttempt);
    
 
    updateUserProgress(userId, course.getId());
    

    QuizResultDTO result = new QuizResultDTO();
    result.setScore(scorePercentage);
    result.setTotalQuestions(totalQuestions);
    result.setCorrectAnswers(correctCount);
    result.setPassed(passed);
    result.setAnswers(questionResults);
    
    achievementService.checkAndUnlockAchievements(userId);

    return result;
}


      @Override
    public List<QuizDTO> getUserQuizHistory(Long userId) {

        UserDTO currentUser = userService.getCurrentUser();  
        if (!currentUser.getRole().equals("ADMIN") && !currentUser.getId().equals(userId)) {
            throw new RuntimeException("Access denied: You can only access your own quiz history");
        }
        
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
   
@Override
public QuizDTO createQuiz(Long lessonId, QuizDTO quizDTO) {
    Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));
    
    Quiz quiz = new Quiz();
    quiz.setTitle(quizDTO.getTitle());
    quiz.setTotalQuestions(0); 
    quiz.setLesson(lesson);
    
    Quiz savedQuiz = quizRepository.save(quiz);
    return convertToDTO(savedQuiz);
}

@Override
public QuestionDTO addQuestionToQuiz(Long quizId, QuestionDTO questionDTO) {
    Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));
    
    Question question = new Question();
    question.setQuestionText(questionDTO.getQuestionText());
    question.setOptionA(questionDTO.getOptionA());
    question.setOptionB(questionDTO.getOptionB());
    question.setOptionC(questionDTO.getOptionC());
    question.setOptionD(questionDTO.getOptionD());
    question.setCorrectAnswer(questionDTO.getCorrectAnswer());
    question.setQuiz(quiz);
    
    Question savedQuestion = questionRepository.save(question);
    
  
    quiz.setTotalQuestions(quiz.getTotalQuestions() + 1);
    quizRepository.save(quiz);
    
    return convertToQuestionDTO(savedQuestion);
}


private QuestionDTO convertToQuestionDTO(Question question) {
    QuestionDTO dto = new QuestionDTO();
    dto.setId(question.getId());
    dto.setQuestionText(question.getQuestionText());
    dto.setOptionA(question.getOptionA());
    dto.setOptionB(question.getOptionB());
    dto.setOptionC(question.getOptionC());
    dto.setOptionD(question.getOptionD());
    dto.setCorrectAnswer(question.getCorrectAnswer()); 
    return dto;
}

    private int calculateScore(Quiz quiz, QuizSubmissionDTO submission) {
        int correctCount = 0;

        for (QuizSubmissionDTO.QuestionAnswerDTO answer : submission.getAnswers()) {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + answer.getQuestionId()));

            
            if (question.getCorrectAnswer().equals(answer.getSelectedAnswer())) {
                correctCount++;
            }
        }

        return correctCount;
    }

    private void updateUserProgress(Long userId, Long courseId) {
    Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for user and course"));

    Course course = enrollment.getCourse();
    
   
    List<Lesson> lessonsWithQuizzes = course.getLessons().stream()
            .filter(lesson -> lesson.getQuiz() != null)
            .collect(Collectors.toList());
    
    long totalQuizzes = lessonsWithQuizzes.size();
    
  
    long completedQuizzes = lessonsWithQuizzes.stream()
            .filter(lesson -> !quizRepository.findByUserIdAndLessonId(userId, lesson.getId()).isEmpty())
            .count();

    double progress = totalQuizzes > 0 ? ((double) completedQuizzes / totalQuizzes) * 100 : 0;
    enrollment.setProgress(progress);
    enrollment.setCompleted(progress >= 100.0);

    enrollmentRepository.save(enrollment);
}


    private QuizDTO convertToDTO(Quiz quiz) {
        QuizDTO dto = new QuizDTO();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setAttemptedAt(quiz.getAttemptedAt());
        dto.setScore(quiz.getScore());
        dto.setTotalQuestions(quiz.getTotalQuestions());
        
        
        if (quiz.getUser() != null) {
            dto.setUserId(quiz.getUser().getId());
        }
        
        
        if (quiz.getLesson() != null) {
            dto.setLessonId(quiz.getLesson().getId());
        }
        
        
        if (quiz.getQuestions() != null && !quiz.getQuestions().isEmpty()) {
            List<QuestionDTO> questionDTOs = quiz.getQuestions().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            dto.setQuestions(questionDTOs);
        }
        
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
    
   
    List<String> options = new ArrayList<>();
    options.add(question.getOptionA());
    options.add(question.getOptionB());
    options.add(question.getOptionC());
    options.add(question.getOptionD());
    dto.setOptions(options);
    
    return dto;
}

}