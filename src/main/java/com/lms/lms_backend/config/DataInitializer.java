package com.lms.lms_backend.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lms.lms_backend.entity.Achievement;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.Lesson;
import com.lms.lms_backend.entity.Question;
import com.lms.lms_backend.entity.Quiz;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.repository.AchievementRepository;
import com.lms.lms_backend.repository.CourseRepository;
import com.lms.lms_backend.repository.LessonRepository;
import com.lms.lms_backend.repository.QuestionRepository;
import com.lms.lms_backend.repository.QuizRepository;
import com.lms.lms_backend.repository.UserRepository;

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
    
    @Autowired
    private AchievementRepository achievementRepository;


    @Override
    public void run(String... args) throws Exception {
        initializeSampleData();
        initializeAchievements();
    }

    private void initializeSampleData() {
        
        if (userRepository.count() == 0) {
            User student = new User("John Doe", "john@student.com", 
                passwordEncoder.encode("password123"), "STUDENT");
            User instructor = new User("Jane Smith", "jane@instructor.com", 
                passwordEncoder.encode("password123"), "INSTRUCTOR");
            
            userRepository.saveAll(Arrays.asList(student, instructor));
        }

        
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

            
            Quiz javaQuiz1 = new Quiz("Java Basics Assessment", 0, 3);
            javaQuiz1.setLesson(javaLesson1);

            quizRepository.save(javaQuiz1);

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
    private void initializeAchievements() {
    if (achievementRepository.count() > 0) {
        return; 
    }

    
    achievementRepository.save(new Achievement(
        "First Steps",
        "Enroll in your first course",
        "assets/achievements/myfirststep.png",
        "COURSE",
        1,
        10
    ));

    achievementRepository.save(new Achievement(
        "Knowledge Seeker",
        "Enroll in 3 courses",
        "assets/achievements/books2.png",
        "COURSE",
        3,
        25
    ));

    achievementRepository.save(new Achievement(
        "Learning Enthusiast",
        "Enroll in 5 courses",
        "assets/achievements/learning.png",
        "COURSE",
        5,
        50
    ));

   
    achievementRepository.save(new Achievement(
        "Quiz Beginner",
        "Complete your first quiz",
        "assets/achievements/quiz.png",
        "QUIZ",
        1,
        10
    ));

    achievementRepository.save(new Achievement(
        "Quiz Master",
        "Complete 5 quizzes",
        "assets/achievements/achievement.png",
        "QUIZ",
        5,
        30
    ));

    achievementRepository.save(new Achievement(
        "Quiz Legend",
        "Complete 10 quizzes",
        "assets/achievements/crown.png",
        "QUIZ",
        10,
        50
    ));

    
    achievementRepository.save(new Achievement(
        "Perfect Score",
        "Get 100% on a quiz",
        "assets/achievements/start.png",
        "PERFECT",
        1,
        20
    ));

    achievementRepository.save(new Achievement(
        "Perfectionist",
        "Get 100% on 3 quizzes",
        "assets/achievements/perfect.png",
        "PERFECT",
        3,
        40
    ));

    achievementRepository.save(new Achievement(
        "Flawless Victory",
        "Get 100% on 5 quizzes",
        "assets/achievements/diamond.png",
        "PERFECT",
        5,
        75
    ));

    
    achievementRepository.save(new Achievement(
        "Course Completer",
        "Complete your first course",
        "assets/achievements/completed2.png",
        "COMPLETED",
        1,
        30
    ));

    achievementRepository.save(new Achievement(
        "Dedicated Learner",
        "Complete 3 courses",
        "assets/achievements/goal.png",
        "COMPLETED",
        3,
        60
    ));

    achievementRepository.save(new Achievement(
        "Master Scholar",
        "Complete 5 courses",
        "assets/achievements/spaceship.jpg",
        "COMPLETED",
        5,
        100
    ));

    System.out.println("✅ Achievements initialized successfully!");
}

}