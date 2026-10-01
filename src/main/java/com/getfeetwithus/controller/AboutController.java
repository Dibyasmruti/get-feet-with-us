package com.getfeetwithus.controller;

import com.getfeetwithus.entity.OrganizationDetails;
import com.getfeetwithus.service.CartService;
import com.getfeetwithus.service.MeetingService;
import com.getfeetwithus.service.OrganizationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    private final OrganizationService organizationService;
    private final MeetingService meetingService;
    private final CartService cartService;

    public AboutController(OrganizationService organizationService,
                           MeetingService meetingService,
                           CartService cartService) {
        this.organizationService = organizationService;
        this.meetingService = meetingService;
        this.cartService = cartService;
    }

    @GetMapping("/about")
    public String aboutPage(Authentication authentication, Model model) {
        String userId = authentication.getName();
        OrganizationDetails org = organizationService.getOrganizationDetails();
        model.addAttribute("org", org);
        model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
        model.addAttribute("cartCount", cartService.getCartItemCount(userId));
        return "user/about";
    }
}
