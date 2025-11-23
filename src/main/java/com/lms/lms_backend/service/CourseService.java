package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.CourseDTO;
import com.lms.lms_backend.dto.LessonDTO;
import java.util.List;

public interface CourseService {
    List<CourseDTO> getAllCourses();
    CourseDTO getCourseById(Long id);
    List<CourseDTO> getCoursesByCategory(String category);
    List<LessonDTO> getCourseLessons(Long courseId);
    LessonDTO getLessonById(Long lessonId);
}