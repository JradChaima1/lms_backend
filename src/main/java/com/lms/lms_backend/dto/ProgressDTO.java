package com.lms.lms_backend.dto;

public class ProgressDTO {
    private Long courseId;
    private String courseTitle;
    private Double progressPercentage;
    private Integer completedLessons;
    private Integer totalLessons;
    private Integer totalQuizzes;
    private Integer passedQuizzes;
    

    public ProgressDTO() {}
    
    public ProgressDTO(Long courseId, String courseTitle, Double progressPercentage, Integer completedLessons, Integer totalLessons) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.progressPercentage = progressPercentage;
        this.completedLessons = completedLessons;
        this.totalLessons = totalLessons;
    }
    

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    
    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }
    
    public Double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Double progressPercentage) { this.progressPercentage = progressPercentage; }
    
    public Integer getCompletedLessons() { return completedLessons; }
    public void setCompletedLessons(Integer completedLessons) { this.completedLessons = completedLessons; }
    
    public Integer getTotalLessons() { return totalLessons; }
    public void setTotalLessons(Integer totalLessons) { this.totalLessons = totalLessons; }
    
    public Integer getTotalQuizzes() { return totalQuizzes; }
    public void setTotalQuizzes(Integer totalQuizzes) { this.totalQuizzes = totalQuizzes; }
    
    public Integer getPassedQuizzes() { return passedQuizzes; }
    public void setPassedQuizzes(Integer passedQuizzes) { this.passedQuizzes = passedQuizzes; }
}