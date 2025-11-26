package com.lms.lms_backend.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.lms_backend.dto.CourseDTO;
import com.lms.lms_backend.dto.LessonDTO;
import com.lms.lms_backend.dto.QuestionDTO;
import com.lms.lms_backend.dto.QuizDTO;
import com.lms.lms_backend.dto.UserDTO;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.Lesson;
import com.lms.lms_backend.entity.Question;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.repository.CourseRepository;
import com.lms.lms_backend.repository.EnrollmentRepository;
import com.lms.lms_backend.repository.LessonRepository;
import com.lms.lms_backend.repository.QuestionRepository;
import com.lms.lms_backend.repository.QuizRepository;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.service.AdminService;
import com.lms.lms_backend.service.CourseService;
import com.lms.lms_backend.service.QuizService;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private QuizService quizService;

    @Override
    public Map<String, Object> getSystemStats() {
    Map<String, Object> stats = new HashMap<>();
    stats.put("totalUsers", userRepository.count());
    stats.put("totalCourses", courseRepository.count());
    stats.put("totalEnrollments", enrollmentRepository.count());
    
    
    long totalQuizzes = quizRepository.findAll().stream()
            .filter(quiz -> quiz.getUser() == null)
            .count();
    stats.put("totalQuizzes", totalQuizzes);
    
    return stats;
}


    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

   @Override
    public List<CourseDTO> getAllCourses() {
    return courseRepository.findAll().stream()
        .map(this::convertToCourseDTO)
        .collect(Collectors.toList());
    }


    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
    Course course = convertToEntity(courseDTO);
    Course savedCourse = courseRepository.save(course);
    return convertToCourseDTO(savedCourse);
      }

    @Override
    public CourseDTO updateCourse(Long courseId, CourseDTO courseDTO) {
    Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    
    updateEntityFromDTO(course, courseDTO);
    
    Course updatedCourse = courseRepository.save(course);
    return convertToCourseDTO(updatedCourse);
}


    @Override
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        courseRepository.delete(course);
    }

    @Override
    public LessonDTO createLesson(Long courseId, LessonDTO lessonDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDTO.getTitle());
        lesson.setContent(lessonDTO.getContent());
        lesson.setLessonOrder(lessonDTO.getLessonOrder());
        lesson.setVideoUrl(lessonDTO.getVideoUrl());
        lesson.setDuration(lessonDTO.getDuration());
        lesson.setCourse(course);
        
        Lesson savedLesson = lessonRepository.save(lesson);
        return convertToLessonDTO(savedLesson);
    }

    @Override
    public LessonDTO updateLesson(Long lessonId, LessonDTO lessonDTO) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        
        lesson.setTitle(lessonDTO.getTitle());
        lesson.setContent(lessonDTO.getContent());
        lesson.setLessonOrder(lessonDTO.getLessonOrder());
        lesson.setVideoUrl(lessonDTO.getVideoUrl());
        lesson.setDuration(lessonDTO.getDuration());
        
        Lesson updatedLesson = lessonRepository.save(lesson);
        return convertToLessonDTO(updatedLesson);
    }

    @Override
    public void deleteLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        lessonRepository.delete(lesson);
    }

    @Override
    public QuizDTO createQuiz(Long lessonId, QuizDTO quizDTO) {
        return quizService.createQuiz(lessonId, quizDTO);
    }

    @Override
    public QuestionDTO addQuestion(Long quizId, QuestionDTO questionDTO) {
        return quizService.addQuestionToQuiz(quizId, questionDTO);
    }

    @Override
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        questionRepository.delete(question);
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    private LessonDTO convertToLessonDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setLessonOrder(lesson.getLessonOrder());
        dto.setVideoUrl(lesson.getVideoUrl());
        dto.setDuration(lesson.getDuration());
        dto.setCourseId(lesson.getCourse().getId());
        return dto;
    }
   private CourseDTO convertToCourseDTO(Course course) {
    CourseDTO dto = new CourseDTO();
    dto.setId(course.getId());
    dto.setTitle(course.getTitle());
    dto.setDescription(course.getDescription());
    dto.setCategory(course.getCategory());
    dto.setDifficulty(course.getDifficulty());
    dto.setDuration(course.getDuration());
 
    if (course.getLessons() != null) {
        dto.setLessonCount(course.getLessons().size());
    }
    
    return dto;
}
private Course convertToEntity(CourseDTO dto) {
    Course course = new Course();
    course.setTitle(dto.getTitle());
    course.setDescription(dto.getDescription());
    course.setCategory(dto.getCategory());
    course.setDifficulty(dto.getDifficulty());
    course.setDuration(dto.getDuration());
    return course;
}

private void updateEntityFromDTO(Course course, CourseDTO dto) {
    course.setTitle(dto.getTitle());
    course.setDescription(dto.getDescription());
    course.setCategory(dto.getCategory());
    course.setDifficulty(dto.getDifficulty());
    course.setDuration(dto.getDuration());
}


}
