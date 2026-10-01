package com.getfeetwithus.service;

import com.getfeetwithus.entity.MeetingMaster;
import com.getfeetwithus.repository.MeetingMasterRepository;
import com.getfeetwithus.util.IdGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MeetingService {

    private final MeetingMasterRepository meetingMasterRepository;
    private final IdGeneratorUtil idGeneratorUtil;

    public MeetingService(MeetingMasterRepository meetingMasterRepository, IdGeneratorUtil idGeneratorUtil) {
        this.meetingMasterRepository = meetingMasterRepository;
        this.idGeneratorUtil = idGeneratorUtil;
    }

    public List<MeetingMaster> getTodayMeetings() {
        return meetingMasterRepository.findByMeetingDateAndIsActiveTrueAndIsCancelledFalseOrderByStartTimeAsc(LocalDate.now());
    }

    public List<MeetingMaster> getPastMeetings() {
        return meetingMasterRepository.findByMeetingDateLessThanAndIsActiveTrueOrderByMeetingDateDescStartTimeDesc(LocalDate.now());
    }

    public boolean hasTodayMeeting() {
        return meetingMasterRepository.existsByMeetingDateAndIsActiveTrueAndIsCancelledFalse(LocalDate.now());
    }

    public List<MeetingMaster> getAllMeetings() {
        return meetingMasterRepository.findAllByOrderByMeetingDateDescStartTimeDesc();
    }

    public Optional<MeetingMaster> findByMeetingId(String meetingId) {
        return meetingMasterRepository.findByMeetingId(meetingId);
    }

    @Transactional
    public MeetingMaster saveMeeting(MeetingMaster meeting) {
        if (meeting.getMeetingId() == null || meeting.getMeetingId().isBlank()) {
            meeting.setMeetingId(idGeneratorUtil.generateMeetingId());
        }
        return meetingMasterRepository.save(meeting);
    }

    @Transactional
    public void toggleMeetingStatus(String meetingId) {
        MeetingMaster meeting = meetingMasterRepository.findByMeetingId(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found: " + meetingId));
        meeting.setIsActive(!Boolean.TRUE.equals(meeting.getIsActive()));
        meetingMasterRepository.save(meeting);
    }

    @Transactional
    public void cancelMeeting(String meetingId) {
        MeetingMaster meeting = meetingMasterRepository.findByMeetingId(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found: " + meetingId));
        meeting.setIsCancelled(true);
        meetingMasterRepository.save(meeting);
    }
}
