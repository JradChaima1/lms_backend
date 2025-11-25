package com.lms.lms_backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.lms_backend.dto.CourseDTO;
import com.lms.lms_backend.dto.LessonDTO;
import com.lms.lms_backend.dto.UserDTO;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.Lesson;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.repository.CourseRepository;
import com.lms.lms_backend.repository.EnrollmentRepository;
import com.lms.lms_backend.repository.LessonRepository;
import com.lms.lms_backend.service.CourseService;
import com.lms.lms_backend.service.UserService;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private UserService userService;

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
   
@Override
public CourseDTO createCourse(CourseDTO courseDTO) {
    Course course = new Course();
    course.setTitle(courseDTO.getTitle());
    course.setDescription(courseDTO.getDescription());
    course.setCategory(courseDTO.getCategory());
    course.setDifficulty(courseDTO.getDifficulty());
    course.setDuration(courseDTO.getDuration());
    
    Course savedCourse = courseRepository.save(course);
    return convertToDTO(savedCourse);
}

@Override
public CourseDTO updateCourse(Long courseId, CourseDTO courseDTO) {
    Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
    
    course.setTitle(courseDTO.getTitle());
    course.setDescription(courseDTO.getDescription());
    course.setCategory(courseDTO.getCategory());
    course.setDifficulty(courseDTO.getDifficulty());
    course.setDuration(courseDTO.getDuration());
    
    Course updatedCourse = courseRepository.save(course);
    return convertToDTO(updatedCourse);
}

@Override
public void deleteCourse(Long courseId) {
    Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
    courseRepository.delete(course);
}

@Override
public LessonDTO createLesson(Long courseId, LessonDTO lessonDTO) {
    Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
    
    Lesson lesson = new Lesson();
    lesson.setTitle(lessonDTO.getTitle());
    lesson.setContent(lessonDTO.getContent());
    lesson.setLessonOrder(lessonDTO.getLessonOrder());
    lesson.setVideoUrl(lessonDTO.getVideoUrl());
    lesson.setDuration(lessonDTO.getDuration());
    lesson.setCourse(course);
    
    Lesson savedLesson = lessonRepository.save(lesson);
    return convertToDTO(savedLesson);
}

@Override
public LessonDTO updateLesson(Long lessonId, LessonDTO lessonDTO) {
    Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));
    
    lesson.setTitle(lessonDTO.getTitle());
    lesson.setContent(lessonDTO.getContent());
    lesson.setLessonOrder(lessonDTO.getLessonOrder());
    lesson.setVideoUrl(lessonDTO.getVideoUrl());
    lesson.setDuration(lessonDTO.getDuration());
    
    Lesson updatedLesson = lessonRepository.save(lesson);
    return convertToDTO(updatedLesson);
}

@Override
public void deleteLesson(Long lessonId) {
    Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));
    lessonRepository.delete(lesson);
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
    
    try {
        UserDTO currentUser = userService.getCurrentUser();
        boolean isEnrolled = enrollmentRepository.findByUserIdAndCourseId(
            currentUser.getId(), 
            course.getId()
        ).isPresent();
        dto.setEnrolled(isEnrolled);
    } catch (Exception e) {
        dto.setEnrolled(false);
    }
    
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