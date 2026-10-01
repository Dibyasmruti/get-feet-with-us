package com.getfeetwithus.controller;

import com.getfeetwithus.entity.MeetingMaster;
import com.getfeetwithus.service.CartService;
import com.getfeetwithus.service.MeetingService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MeetingController {

    private final MeetingService meetingService;
    private final CartService cartService;

    public MeetingController(MeetingService meetingService, CartService cartService) {
        this.meetingService = meetingService;
        this.cartService = cartService;
    }

    @GetMapping("/meetings")
    public String meetingsPage(Authentication authentication, Model model) {
        String userId = authentication.getName();
        List<MeetingMaster> todayMeetings = meetingService.getTodayMeetings();
        List<MeetingMaster> pastMeetings = meetingService.getPastMeetings();

        model.addAttribute("todayMeetings", todayMeetings);
        model.addAttribute("pastMeetings", pastMeetings);
        model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
        model.addAttribute("cartCount", cartService.getCartItemCount(userId));

        return "user/meetings";
    }
}
