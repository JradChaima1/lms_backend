package com.lms.lms_backend.service;

import java.util.List;
import java.util.Map;

import com.lms.lms_backend.dto.CourseDTO;
import com.lms.lms_backend.dto.LessonDTO;
import com.lms.lms_backend.dto.QuestionDTO;
import com.lms.lms_backend.dto.QuizDTO;
import com.lms.lms_backend.dto.UserDTO;

public interface AdminService {
    Map<String, Object> getSystemStats();
    List<UserDTO> getAllUsers();
    void deleteUser(Long userId);
    List<CourseDTO> getAllCourses();
    CourseDTO createCourse(CourseDTO courseDTO);
    CourseDTO updateCourse(Long courseId, CourseDTO courseDTO);
    void deleteCourse(Long courseId);
    LessonDTO createLesson(Long courseId, LessonDTO lessonDTO);
    LessonDTO updateLesson(Long lessonId, LessonDTO lessonDTO);
    void deleteLesson(Long lessonId);
    QuizDTO createQuiz(Long lessonId, QuizDTO quizDTO);
    QuizDTO updateQuiz(Long quizId, QuizDTO quizDTO);
    QuestionDTO addQuestion(Long quizId, QuestionDTO questionDTO);
    void deleteQuestion(Long questionId);
}
