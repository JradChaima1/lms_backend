package com.lms.lms_backend.config;

import com.lms.lms_backend.entity.*;
import com.lms.lms_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Create sample users
        if (userRepository.count() == 0) {
            User student = new User("John Doe", "john@student.com", 
                passwordEncoder.encode("password123"), "STUDENT");
            User instructor = new User("Jane Smith", "jane@instructor.com", 
                passwordEncoder.encode("password123"), "INSTRUCTOR");
            
            userRepository.saveAll(Arrays.asList(student, instructor));
        }

        // Create sample courses
        if (courseRepository.count() == 0) {
            Course javaCourse = new Course(
                "Java Programming Fundamentals",
                "Learn core Java programming concepts from scratch",
                "Programming",
                "BEGINNER",
                40
            );

            Course springCourse = new Course(
                "Spring Boot Masterclass",
                "Build modern web applications with Spring Boot",
                "Web Development",
                "INTERMEDIATE",
                30
            );

            courseRepository.saveAll(Arrays.asList(javaCourse, springCourse));

            // Create lessons for Java course
            Lesson javaLesson1 = new Lesson(
                "Introduction to Java",
                "Java is a high-level, class-based, object-oriented programming language...",
                1,
                "https://example.com/video1",
                45
            );
            javaLesson1.setCourse(javaCourse);

            Lesson javaLesson2 = new Lesson(
                "Object-Oriented Programming",
                "Learn about classes, objects, inheritance, and polymorphism...",
                2,
                "https://example.com/video2",
                60
            );
            javaLesson2.setCourse(javaCourse);

            lessonRepository.saveAll(Arrays.asList(javaLesson1, javaLesson2));

            // Create quiz for first lesson
            Quiz javaQuiz1 = new Quiz("Java Basics Assessment", 0, 3);
            javaQuiz1.setLesson(javaLesson1);

            quizRepository.save(javaQuiz1);

            // Create questions for quiz
            Question question1 = new Question(
                "What is the main purpose of Java?",
                "Game Development",
                "Web Browsers", 
                "Platform-independent applications",
                "Mobile Games",
                "C"
            );
            question1.setQuiz(javaQuiz1);

            Question question2 = new Question(
                "Which keyword is used to define a class in Java?",
                "class",
                "interface",
                "abstract",
                "void",
                "A"
            );
            question2.setQuiz(javaQuiz1);

            questionRepository.saveAll(Arrays.asList(question1, question2));
        }
    }
}