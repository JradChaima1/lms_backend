package com.lms.lms_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.lms_backend.dto.AchievementDTO;
import com.lms.lms_backend.service.AchievementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/achievements")
@Tag(name = "Achievement Management", description = "APIs for user achievements and gamification")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @GetMapping
    @Operation(
        summary = "Get all achievements",
        description = "Retrieve list of all available achievements"
    )
    public ResponseEntity<List<AchievementDTO>> getAllAchievements() {
        List<AchievementDTO> achievements = achievementService.getAllAchievements();
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/me")
    @Operation(
        summary = "Get my achievements",
        description = "Retrieve achievements for the currently authenticated user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<AchievementDTO>> getMyAchievements() {
        List<AchievementDTO> achievements = achievementService.getMyAchievements();
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/{userId}")
    @Operation(
        summary = "Get user achievements",
        description = "Retrieve achievements for a specific user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<AchievementDTO>> getUserAchievements(@PathVariable Long userId) {
        List<AchievementDTO> achievements = achievementService.getUserAchievements(userId);
        return ResponseEntity.ok(achievements);
    }

    @PostMapping("/check")
    @Operation(
        summary = "Check and unlock achievements",
        description = "Check if user has met requirements for any new achievements",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> checkAchievements() {
        achievementService.checkAndUnlockAchievements(
            achievementService.getMyAchievements().get(0).getId() // This needs proper user ID
        );
        return ResponseEntity.ok().build();
    }
}
