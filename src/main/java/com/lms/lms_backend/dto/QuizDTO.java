package com.lms.lms_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class QuizDTO {
    private Long id;
    private String title;
    private LocalDateTime attemptedAt;
    private Integer score;
    private Integer totalQuestions;
    private Long userId;
    private Long lessonId;
    private List<QuestionDTO> questions;
    

    public QuizDTO() {}
    
    public QuizDTO(Long id, String title, LocalDateTime attemptedAt, Integer score, Integer totalQuestions, Long userId, Long lessonId) {
        this.id = id;
        this.title = title;
        this.attemptedAt = attemptedAt;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.userId = userId;
        this.lessonId = lessonId;
    }
    
   
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public LocalDateTime getAttemptedAt() { return attemptedAt; }
    public void setAttemptedAt(LocalDateTime attemptedAt) { this.attemptedAt = attemptedAt; }
    
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }
    
    public List<QuestionDTO> getQuestions() { return questions; }
    public void setQuestions(List<QuestionDTO> questions) { this.questions = questions; }
}