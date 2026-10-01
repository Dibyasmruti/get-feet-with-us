package com.getfeetwithus.repository;

import com.getfeetwithus.entity.MeetingMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingMasterRepository extends JpaRepository<MeetingMaster, Long> {
    Optional<MeetingMaster> findByMeetingId(String meetingId);
    List<MeetingMaster> findByMeetingDateAndIsActiveTrueAndIsCancelledFalseOrderByStartTimeAsc(LocalDate meetingDate);
    List<MeetingMaster> findByMeetingDateLessThanAndIsActiveTrueOrderByMeetingDateDescStartTimeDesc(LocalDate meetingDate);
    List<MeetingMaster> findAllByOrderByMeetingDateDescStartTimeDesc();
    boolean existsByMeetingDateAndIsActiveTrueAndIsCancelledFalse(LocalDate meetingDate);
}
