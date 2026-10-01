package com.getfeetwithus.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "diet_master")
public class DietMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "diet_id", unique = true, nullable = false, length = 50)
    private String dietId;

    @Column(name = "diet_date", nullable = false)
    private LocalDate dietDate;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String tips;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "other_instructions", columnDefinition = "TEXT")
    private String otherInstructions;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public DietMaster() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDietId() { return dietId; }
    public void setDietId(String dietId) { this.dietId = dietId; }
    public LocalDate getDietDate() { return dietDate; }
    public void setDietDate(LocalDate dietDate) { this.dietDate = dietDate; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTips() { return tips; }
    public void setTips(String tips) { this.tips = tips; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public String getOtherInstructions() { return otherInstructions; }
    public void setOtherInstructions(String otherInstructions) { this.otherInstructions = otherInstructions; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
