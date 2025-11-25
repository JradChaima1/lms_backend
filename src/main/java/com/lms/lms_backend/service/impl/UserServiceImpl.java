package com.lms.lms_backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.lms_backend.dto.CourseDTO;
import com.lms.lms_backend.dto.LoginRequest;
import com.lms.lms_backend.dto.ProgressDTO;
import com.lms.lms_backend.dto.RegistrationRequest;
import com.lms.lms_backend.dto.UserDTO;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.Enrollment;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.exception.UserAlreadyExistsException;
import com.lms.lms_backend.repository.CourseRepository;
import com.lms.lms_backend.repository.EnrollmentRepository;
import com.lms.lms_backend.repository.QuizRepository;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.service.CourseService;
import com.lms.lms_backend.service.UserService;


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
    @Autowired
    @Lazy
    private CourseService courseService;


    @Override
    public UserDTO registerUser(RegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + registrationRequest.getEmail() + " already exists");
        }

        User user = new User();
        user.setName(registrationRequest.getFirstName() + " " + registrationRequest.getLastName());

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
        // Get current user to check permissions
        UserDTO currentUser = getCurrentUser();
        
        // Security: Users can only access their own data unless they're ADMIN
        if (!currentUser.getRole().equals("ADMIN") && !currentUser.getId().equals(id)) {
            throw new RuntimeException("Access denied: You can only access your own user data");
        }
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }

    @Override
    public UserDTO getCurrentUser() {
        // Get the authenticated user's email from Spring Security context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        return convertToDTO(user);
    }

    @Override
    public List<ProgressDTO> getUserProgress(Long userId) {
        // Add security check here too
        UserDTO currentUser = getCurrentUser();
        
        // Security: Users can only access their own progress unless they're ADMIN
        if (!currentUser.getRole().equals("ADMIN") && !currentUser.getId().equals(userId)) {
            throw new RuntimeException("Access denied: You can only access your own progress data");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);

        return enrollments.stream()
                .map(this::convertToProgressDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProgressDTO> getMyProgress() {
        // Get current user and return their progress
        UserDTO currentUser = getCurrentUser();
        return getUserProgress(currentUser.getId());
    }

    @Override
    public void enrollInCourse(Long userId, Long courseId) {
        // Add security check for admin-only functionality
        UserDTO currentUser = getCurrentUser();
        
        // Security: Only ADMIN can enroll other users, users can only enroll themselves
        if (!currentUser.getRole().equals("ADMIN") && !currentUser.getId().equals(userId)) {
            throw new RuntimeException("Access denied: You can only enroll yourself in courses");
        }
        
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

    @Override
    public void enrollInCourse(Long courseId) {
        // Overloaded method for current user enrollment
        UserDTO currentUser = getCurrentUser();
        enrollInCourse(currentUser.getId(), courseId);
    }
    

    @Override
    public List<CourseDTO> getMyEnrolledCourses() {
    UserDTO currentUser = getCurrentUser();
    List<Enrollment> enrollments = enrollmentRepository.findByUserId(currentUser.getId());
    
    return enrollments.stream()
            .map(enrollment -> courseService.getCourseById(enrollment.getCourse().getId()))
            .collect(Collectors.toList());
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