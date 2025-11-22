package com.lms.lms_backend.dto;

public class LessonDTO {
    private Long id;
    private String title;
    private String content;
    private Integer lessonOrder;
    private String videoUrl;
    private Integer duration;
    private Long courseId;
    private Boolean hasQuiz;
    

    public LessonDTO() {}
    
    public LessonDTO(Long id, String title, String content, Integer lessonOrder, String videoUrl, Integer duration, Long courseId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.lessonOrder = lessonOrder;
        this.videoUrl = videoUrl;
        this.duration = duration;
        this.courseId = courseId;
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
    
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    
    public Boolean getHasQuiz() { return hasQuiz; }
    public void setHasQuiz(Boolean hasQuiz) { this.hasQuiz = hasQuiz; }
}