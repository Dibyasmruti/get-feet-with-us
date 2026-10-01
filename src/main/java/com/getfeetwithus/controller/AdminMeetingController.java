package com.getfeetwithus.controller;

import com.getfeetwithus.entity.MeetingMaster;
import com.getfeetwithus.service.MeetingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class AdminMeetingController {

    private final MeetingService meetingService;

    public AdminMeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping("/admin/meetings")
    public String listMeetings(Model model) {
        model.addAttribute("meetings", meetingService.getAllMeetings());
        return "admin/meetings";
    }

    @GetMapping("/admin/meetings/new")
    public String newMeetingForm(Model model) {
        MeetingMaster meeting = new MeetingMaster();
        meeting.setMeetingDate(LocalDate.now());
        meeting.setStartTime(LocalTime.of(10, 0));
        meeting.setDuration("45 Mins");
        model.addAttribute("meeting", meeting);
        return "admin/meeting_form";
    }

    @GetMapping("/admin/meetings/edit/{meetingId}")
    public String editMeetingForm(@PathVariable("meetingId") String meetingId, Model model) {
        MeetingMaster meeting = meetingService.findByMeetingId(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found: " + meetingId));
        model.addAttribute("meeting", meeting);
        return "admin/meeting_form";
    }

    @PostMapping("/admin/meetings/save")
    public String saveMeeting(@ModelAttribute("meeting") MeetingMaster meeting,
                              RedirectAttributes redirectAttributes) {
        try {
            meetingService.saveMeeting(meeting);
            redirectAttributes.addFlashAttribute("successMessage", "Meeting saved successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving meeting: " + ex.getMessage());
        }
        return "redirect:/admin/meetings";
    }

    @PostMapping("/admin/meetings/toggle/{meetingId}")
    public String toggleMeetingStatus(@PathVariable("meetingId") String meetingId, RedirectAttributes redirectAttributes) {
        try {
            meetingService.toggleMeetingStatus(meetingId);
            redirectAttributes.addFlashAttribute("successMessage", "Meeting status updated.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/meetings";
    }

    @PostMapping("/admin/meetings/cancel/{meetingId}")
    public String cancelMeeting(@PathVariable("meetingId") String meetingId, RedirectAttributes redirectAttributes) {
        try {
            meetingService.cancelMeeting(meetingId);
            redirectAttributes.addFlashAttribute("successMessage", "Meeting marked as cancelled.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/meetings";
    }
}
