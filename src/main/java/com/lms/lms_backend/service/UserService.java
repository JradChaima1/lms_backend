package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.UserDTO;
import com.lms.lms_backend.dto.LoginRequest;
import com.lms.lms_backend.dto.ProgressDTO;
import java.util.List;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);
    UserDTO login(LoginRequest loginRequest);
    UserDTO getUserById(Long id);
    List<ProgressDTO> getUserProgress(Long userId);
    void enrollInCourse(Long userId, Long courseId);
}