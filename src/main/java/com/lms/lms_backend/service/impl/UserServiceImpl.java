package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.UserDTO;
import com.lms.lms_backend.dto.LoginRequest;
import com.lms.lms_backend.dto.ProgressDTO;
import com.lms.lms_backend.dto.RegistrationRequest;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.entity.Enrollment;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.repository.EnrollmentRepository;
import com.lms.lms_backend.repository.CourseRepository;
import com.lms.lms_backend.repository.QuizRepository;
import com.lms.lms_backend.service.UserService;
import com.lms.lms_backend.exception.UserAlreadyExistsException;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO registerUser(RegistrationRequest registrationRequest) {

        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + registrationRequest.getEmail() + " already exists");
        }

    
        User user = new User();
        user.setName(registrationRequest.getName());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(registrationRequest.getRole() != null ? registrationRequest.getRole() : "STUDENT");

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail()));

      
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return convertToDTO(user);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }

    @Override
    public List<ProgressDTO> getUserProgress(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);

        return enrollments.stream()
                .map(this::convertToProgressDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void enrollInCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        
        if (enrollmentRepository.findByUserIdAndCourseId(userId, courseId).isPresent()) {
            throw new RuntimeException("User already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment(user, course);
        enrollmentRepository.save(enrollment);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    private ProgressDTO convertToProgressDTO(Enrollment enrollment) {
        Course course = enrollment.getCourse();
        
   
        int totalLessons = course.getLessons().size();
        int completedLessons = calculateCompletedLessons(enrollment.getUser(), course);
        double progressPercentage = totalLessons > 0 ? (double) completedLessons / totalLessons * 100 : 0;

        ProgressDTO progressDTO = new ProgressDTO();
        progressDTO.setCourseId(course.getId());
        progressDTO.setCourseTitle(course.getTitle());
        progressDTO.setProgressPercentage(progressPercentage);
        progressDTO.setCompletedLessons(completedLessons);
        progressDTO.setTotalLessons(totalLessons);
        
        return progressDTO;
    }

    private int calculateCompletedLessons(User user, Course course) {
    
        return (int) course.getLessons().stream()
                .filter(lesson -> lesson.getQuiz() != null)
                .filter(lesson -> quizRepository.findByUserIdAndLessonId(user.getId(), lesson.getId()).size() > 0)
                .count();
    }
}