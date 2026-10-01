package com.getfeetwithus.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_master")
public class FitnessMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fitness_id", unique = true, nullable = false, length = 50)
    private String fitnessId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(length = 50)
    private String duration;

    @Column(length = 50)
    private String repetitions;

    @Column(name = "other_instructions", columnDefinition = "TEXT")
    private String otherInstructions;

    @Column(length = 50)
    private String category = "General"; // Yoga, Cardio, Strength

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public FitnessMaster() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFitnessId() { return fitnessId; }
    public void setFitnessId(String fitnessId) { this.fitnessId = fitnessId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getRepetitions() { return repetitions; }
    public void setRepetitions(String repetitions) { this.repetitions = repetitions; }
    public String getOtherInstructions() { return otherInstructions; }
    public void setOtherInstructions(String otherInstructions) { this.otherInstructions = otherInstructions; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
