package com.getfeetwithus.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "meeting_master")
public class MeetingMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meeting_id", unique = true, nullable = false, length = 50)
    private String meetingId;

    @Column(nullable = false, length = 250)
    private String topic;

    @Column(name = "host_details", nullable = false, length = 200)
    private String hostDetails;

    @Column(columnDefinition = "TEXT")
    private String agenda;

    @Column(name = "meeting_date", nullable = false)
    private LocalDate meetingDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(length = 50)
    private String duration;

    @Column(name = "meeting_mode", nullable = false, length = 50)
    private String meetingMode = "Online"; // Online, Offline, Hybrid

    @Column(name = "meeting_address", columnDefinition = "TEXT")
    private String meetingAddress;

    @Column(name = "meeting_link", length = 500)
    private String meetingLink;

    @Column(name = "other_details", columnDefinition = "TEXT")
    private String otherDetails;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_cancelled")
    private Boolean isCancelled = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public MeetingMaster() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getHostDetails() { return hostDetails; }
    public void setHostDetails(String hostDetails) { this.hostDetails = hostDetails; }
    public String getAgenda() { return agenda; }
    public void setAgenda(String agenda) { this.agenda = agenda; }
    public LocalDate getMeetingDate() { return meetingDate; }
    public void setMeetingDate(LocalDate meetingDate) { this.meetingDate = meetingDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getMeetingMode() { return meetingMode; }
    public void setMeetingMode(String meetingMode) { this.meetingMode = meetingMode; }
    public String getMeetingAddress() { return meetingAddress; }
    public void setMeetingAddress(String meetingAddress) { this.meetingAddress = meetingAddress; }
    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    public String getOtherDetails() { return otherDetails; }
    public void setOtherDetails(String otherDetails) { this.otherDetails = otherDetails; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Boolean getIsCancelled() { return isCancelled; }
    public void setIsCancelled(Boolean isCancelled) { this.isCancelled = isCancelled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
