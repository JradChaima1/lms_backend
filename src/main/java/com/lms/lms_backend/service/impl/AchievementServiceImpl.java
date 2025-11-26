package com.lms.lms_backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.lms_backend.dto.AchievementDTO;
import com.lms.lms_backend.dto.UserDTO;
import com.lms.lms_backend.entity.Achievement;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.entity.UserAchievement;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.repository.AchievementRepository;
import com.lms.lms_backend.repository.EnrollmentRepository;
import com.lms.lms_backend.repository.QuizRepository;
import com.lms.lms_backend.repository.UserAchievementRepository;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.service.AchievementService;
import com.lms.lms_backend.service.UserService;

@Service
@Transactional
public class AchievementServiceImpl implements AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<AchievementDTO> getAllAchievements() {
        return achievementRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AchievementDTO> getUserAchievements(Long userId) {
        UserDTO currentUser = userService.getCurrentUser();
        if (!currentUser.getRole().equals("ADMIN") && !currentUser.getId().equals(userId)) {
            throw new RuntimeException("Access denied: You can only view your own achievements");
        }

        List<Achievement> allAchievements = achievementRepository.findAll();
        
        return allAchievements.stream()
                .map(achievement -> convertToDTOWithProgress(achievement, userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<AchievementDTO> getMyAchievements() {
        UserDTO currentUser = userService.getCurrentUser();
        return getUserAchievements(currentUser.getId());
    }

    @Override
    public void checkAndUnlockAchievements(Long userId) {
        List<Achievement> allAchievements = achievementRepository.findAll();
        
        for (Achievement achievement : allAchievements) {
            
            if (userAchievementRepository.existsByUserIdAndAchievementId(userId, achievement.getId())) {
                continue;
            }
            
            
            int currentProgress = calculateProgress(userId, achievement);
            if (currentProgress >= achievement.getRequiredCount()) {
                unlockAchievement(userId, achievement.getId());
            }
        }
    }

    @Override
    public AchievementDTO unlockAchievement(Long userId, Long achievementId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found"));
        
     
        if (userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)) {
            return convertToDTOWithProgress(achievement, userId);
        }
        
       
        UserAchievement userAchievement = new UserAchievement(user, achievement);
        userAchievementRepository.save(userAchievement);
        
        return convertToDTOWithProgress(achievement, userId);
    }

    private int calculateProgress(Long userId, Achievement achievement) {
        switch (achievement.getCategory()) {
            case "COURSE":
                return (int) enrollmentRepository.countByUserId(userId);
            
            case "QUIZ":
               
                return (int) quizRepository.findByUserId(userId).stream()
                        .map(quiz -> quiz.getLesson().getId())
                        .distinct()
                        .count();
            
            case "PERFECT":
            
                return (int) quizRepository.findByUserId(userId).stream()
                        .filter(quiz -> {
                            int totalQuestions = quiz.getTotalQuestions();
                            int score = quiz.getScore();
                            return totalQuestions > 0 && score == totalQuestions;
                        })
                        .count();
            
            case "COMPLETED":
             
                return (int) enrollmentRepository.findByUserId(userId).stream()
                        .filter(enrollment -> enrollment.getCompleted() != null && enrollment.getCompleted())
                        .count();
            
            default:
                return 0;
        }
    }

    private AchievementDTO convertToDTO(Achievement achievement) {
        AchievementDTO dto = new AchievementDTO();
        dto.setId(achievement.getId());
        dto.setName(achievement.getName());
        dto.setDescription(achievement.getDescription());
        dto.setIcon(achievement.getIcon());
        dto.setCategory(achievement.getCategory());
        dto.setRequiredCount(achievement.getRequiredCount());
        dto.setPoints(achievement.getPoints());
        dto.setUnlocked(false);
        dto.setCurrentProgress(0);
        return dto;
    }

    private AchievementDTO convertToDTOWithProgress(Achievement achievement, Long userId) {
        AchievementDTO dto = convertToDTO(achievement);
        
    
        UserAchievement userAchievement = userAchievementRepository
                .findByUserIdAndAchievementId(userId, achievement.getId())
                .orElse(null);
        
        if (userAchievement != null) {
            dto.setUnlocked(true);
            dto.setUnlockedAt(userAchievement.getUnlockedAt());
            dto.setCurrentProgress(achievement.getRequiredCount());
        } else {
            dto.setUnlocked(false);
            dto.setCurrentProgress(calculateProgress(userId, achievement));
        }
        
        return dto;
    }
}
