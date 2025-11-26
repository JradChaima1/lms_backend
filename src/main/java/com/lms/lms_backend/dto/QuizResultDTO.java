package com.lms.lms_backend.dto;

import java.util.List;

public class QuizResultDTO {
    private Integer score;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Boolean passed;
    private List<QuestionResultDTO> answers;
    
    public QuizResultDTO() {}
    
    public QuizResultDTO(Integer score, Integer totalQuestions, Integer correctAnswers, Boolean passed, List<QuestionResultDTO> answers) {
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.passed = passed;
        this.answers = answers;
    }
    
    
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }
    
    public Integer getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(Integer correctAnswers) { this.correctAnswers = correctAnswers; }
    
    public Boolean getPassed() { return passed; }
    public void setPassed(Boolean passed) { this.passed = passed; }
    
    public List<QuestionResultDTO> getAnswers() { return answers; }
    public void setAnswers(List<QuestionResultDTO> answers) { this.answers = answers; }
}
