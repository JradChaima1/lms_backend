package com.lms.lms_backend.repository;

import com.lms.lms_backend.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByUserId(Long userId);
    List<Quiz> findByUserIdAndLessonId(Long userId, Long lessonId);
    Optional<Quiz> findByLessonId(Long lessonId);
}