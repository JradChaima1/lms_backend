package com.lms.lms_backend.repository;

import com.lms.lms_backend.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;  
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseIdOrderByLessonOrder(Long courseId);
    Optional<Lesson> findByCourseIdAndLessonOrder(Long courseId, Integer lessonOrder);
}