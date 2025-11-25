package com.lms.lms_backend.service;

import java.util.List;

import com.lms.lms_backend.dto.CourseDTO;
import com.lms.lms_backend.dto.LoginRequest;
import com.lms.lms_backend.dto.ProgressDTO;
import com.lms.lms_backend.dto.RegistrationRequest;
import com.lms.lms_backend.dto.UserDTO;

public interface UserService {
    UserDTO registerUser(RegistrationRequest registrationRequest);
    UserDTO login(LoginRequest loginRequest);
    UserDTO getUserById(Long id);
    UserDTO getCurrentUser(); 
    List<ProgressDTO> getUserProgress(Long userId);
    List<ProgressDTO> getMyProgress();  
    void enrollInCourse(Long userId, Long courseId);
    void enrollInCourse(Long courseId);
    List<CourseDTO> getMyEnrolledCourses();
  
}