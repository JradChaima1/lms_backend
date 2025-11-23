package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.CourseDTO;
import com.lms.lms_backend.dto.LessonDTO;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.Lesson;
import com.lms.lms_backend.repository.CourseRepository;
import com.lms.lms_backend.repository.LessonRepository;
import com.lms.lms_backend.repository.EnrollmentRepository;
import com.lms.lms_backend.service.CourseService;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return convertToDTO(course);
    }

    @Override
    public List<CourseDTO> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonDTO> getCourseLessons(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        
        return lessonRepository.findByCourseIdOrderByLessonOrder(courseId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LessonDTO getLessonById(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));
        return convertToDTO(lesson);
    }

    private CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setCategory(course.getCategory());
        dto.setDifficulty(course.getDifficulty());
        dto.setDuration(course.getDuration());
        dto.setLessonCount(course.getLessons().size());
        dto.setEnrollmentCount(enrollmentRepository.countByCourseId(course.getId()));
        return dto;
    }

    private LessonDTO convertToDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setLessonOrder(lesson.getLessonOrder());
        dto.setVideoUrl(lesson.getVideoUrl());
        dto.setDuration(lesson.getDuration());
        dto.setCourseId(lesson.getCourse().getId());
        dto.setHasQuiz(lesson.getQuiz() != null);
        return dto;
    }
}