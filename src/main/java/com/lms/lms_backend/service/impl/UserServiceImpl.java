package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.UserDTO;
import com.lms.lms_backend.dto.LoginRequest;
import com.lms.lms_backend.dto.ProgressDTO;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.entity.Enrollment;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.Lesson;
import com.lms.lms_backend.entity.Quiz;
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
    public UserDTO registerUser(UserDTO userDTO) {
        // Check if user already exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + userDTO.getEmail() + " already exists");
        }

        // Create new user entity
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Encode password
        user.setRole("STUDENT"); // Default role

        User savedUser = userRepository.save(user);

        // Convert to DTO and return
        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail()));

        // Verify password
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

        // Check if already enrolled
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
        
        // Calculate progress metrics
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
        // A lesson is considered completed if the user has taken its quiz
        return (int) course.getLessons().stream()
                .filter(lesson -> lesson.getQuiz() != null)
                .filter(lesson -> quizRepository.findByUserIdAndLessonId(user.getId(), lesson.getId()).size() > 0)
                .count();
    }
}