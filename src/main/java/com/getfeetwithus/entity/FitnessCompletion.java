package com.getfeetwithus.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_completion", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "fitness_id", "completion_date"})
})
public class FitnessCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "user_name", length = 150)
    private String userName;

    @Column(name = "fitness_id", nullable = false, length = 50)
    private String fitnessId;

    @Column(name = "fitness_title", length = 200)
    private String fitnessTitle;

    @Column(name = "completion_date", nullable = false)
    private LocalDate completionDate;

    @Column(name = "completion_time", nullable = false)
    private LocalTime completionTime;

    @Column(name = "completion_status", length = 50)
    private String completionStatus = "COMPLETED";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public FitnessCompletion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getFitnessId() { return fitnessId; }
    public void setFitnessId(String fitnessId) { this.fitnessId = fitnessId; }
    public String getFitnessTitle() { return fitnessTitle; }
    public void setFitnessTitle(String fitnessTitle) { this.fitnessTitle = fitnessTitle; }
    public LocalDate getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDate completionDate) { this.completionDate = completionDate; }
    public LocalTime getCompletionTime() { return completionTime; }
    public void setCompletionTime(LocalTime completionTime) { this.completionTime = completionTime; }
    public String getCompletionStatus() { return completionStatus; }
    public void setCompletionStatus(String completionStatus) { this.completionStatus = completionStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
