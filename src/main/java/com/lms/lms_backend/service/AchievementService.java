package com.lms.lms_backend.service;

import java.util.List;

import com.lms.lms_backend.dto.AchievementDTO;

public interface AchievementService {
    List<AchievementDTO> getAllAchievements();
    List<AchievementDTO> getUserAchievements(Long userId);
    List<AchievementDTO> getMyAchievements();
    void checkAndUnlockAchievements(Long userId);
    AchievementDTO unlockAchievement(Long userId, Long achievementId);
}
