package com.lms.lms_backend.dto;

import java.util.List;

public class QuizSubmissionDTO {
    private Long lessonId;
    private List<QuestionAnswerDTO> answers;
    
 
    public QuizSubmissionDTO() {}
    
    public QuizSubmissionDTO(Long lessonId, List<QuestionAnswerDTO> answers) {
        this.lessonId = lessonId;
        this.answers = answers;
    }
    

    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }
    
    public List<QuestionAnswerDTO> getAnswers() { return answers; }
    public void setAnswers(List<QuestionAnswerDTO> answers) { this.answers = answers; }
    
    public static class QuestionAnswerDTO {
        private Long questionId;
        private String selectedAnswer;
        
     
        public QuestionAnswerDTO() {}
        
        public QuestionAnswerDTO(Long questionId, String selectedAnswer) {
            this.questionId = questionId;
            this.selectedAnswer = selectedAnswer;
        }
        
      
        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        
        public String getSelectedAnswer() { return selectedAnswer; }
        public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }
    }
}