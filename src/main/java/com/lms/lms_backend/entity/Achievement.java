package com.lms.lms_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "achievements")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private String icon;
    
    @Column(nullable = false)
    private String category; 
    
    @Column(nullable = false)
    private Integer requiredCount;
    
    @Column(nullable = false)
    private Integer points;
    
    public Achievement() {}
    
    public Achievement(String name, String description, String icon, String category, Integer requiredCount, Integer points) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.category = category;
        this.requiredCount = requiredCount;
        this.points = points;
    }
    
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Integer getRequiredCount() { return requiredCount; }
    public void setRequiredCount(Integer requiredCount) { this.requiredCount = requiredCount; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
}
