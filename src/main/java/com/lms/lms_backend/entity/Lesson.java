package com.lms.lms_backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String content;
    
    private Integer lessonOrder;
    private String videoUrl;
    private Integer duration; 
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
    
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private java.util.List<Quiz> quizzes;
    
  
    public Lesson() {}
    
    public Lesson(String title, String content, Integer lessonOrder, String videoUrl, Integer duration) {
        this.title = title;
        this.content = content;
        this.lessonOrder = lessonOrder;
        this.videoUrl = videoUrl;
        this.duration = duration;
    }
    
  
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public Integer getLessonOrder() { return lessonOrder; }
    public void setLessonOrder(Integer lessonOrder) { this.lessonOrder = lessonOrder; }
    
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    
    public java.util.List<Quiz> getQuizzes() { return quizzes; }
    public void setQuizzes(java.util.List<Quiz> quizzes) { this.quizzes = quizzes; }
    
    // Helper method to get the template quiz (quiz without user)
    public Quiz getQuiz() { 
        if (quizzes != null && !quizzes.isEmpty()) {
            return quizzes.stream()
                .filter(q -> q.getUser() == null)
                .findFirst()
                .orElse(null);
        }
        return null;
    }
}