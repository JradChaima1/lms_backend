package com.lms.lms_backend.service;

import java.util.List;

import com.lms.lms_backend.dto.CourseDTO;
import com.lms.lms_backend.dto.LessonDTO;

public interface CourseService {
    List<CourseDTO> getAllCourses();
    CourseDTO getCourseById(Long id);
    List<CourseDTO> getCoursesByCategory(String category);
    List<LessonDTO> getCourseLessons(Long courseId);
    LessonDTO getLessonById(Long lessonId);
    CourseDTO createCourse(CourseDTO courseDTO);
    CourseDTO updateCourse(Long courseId, CourseDTO courseDTO);
    void deleteCourse(Long courseId);
    LessonDTO createLesson(Long courseId, LessonDTO lessonDTO);
    LessonDTO updateLesson(Long lessonId, LessonDTO lessonDTO);
    void deleteLesson(Long lessonId);
}