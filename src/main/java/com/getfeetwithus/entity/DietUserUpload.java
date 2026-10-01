package com.getfeetwithus.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "diet_user_upload")
public class DietUserUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "user_name", length = 150)
    private String userName;

    @Column(name = "diet_id", length = 50)
    private String dietId;

    @Column(name = "diet_title", length = 200)
    private String dietTitle;

    @Column(name = "upload_date", nullable = false)
    private LocalDate uploadDate;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime = LocalDateTime.now();

    public DietUserUpload() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getDietId() { return dietId; }
    public void setDietId(String dietId) { this.dietId = dietId; }
    public String getDietTitle() { return dietTitle; }
    public void setDietTitle(String dietTitle) { this.dietTitle = dietTitle; }
    public LocalDate getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDate uploadDate) { this.uploadDate = uploadDate; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getUploadTime() { return uploadTime; }
    public void setUploadTime(LocalDateTime uploadTime) { this.uploadTime = uploadTime; }
}
