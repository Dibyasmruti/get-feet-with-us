package com.getfeetwithus.controller;

import com.getfeetwithus.entity.User;
import com.getfeetwithus.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserDashboardController {

    private final UserService userService;
    private final MeetingService meetingService;
    private final DietService dietService;
    private final FitnessService fitnessService;
    private final CartService cartService;

    public UserDashboardController(UserService userService,
                                   MeetingService meetingService,
                                   DietService dietService,
                                   FitnessService fitnessService,
                                   CartService cartService) {
        this.userService = userService;
        this.meetingService = meetingService;
        this.dietService = dietService;
        this.fitnessService = fitnessService;
        this.cartService = cartService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String userId = authentication.getName();
        User user = userService.findByUserId(userId).orElse(null);
        model.addAttribute("currentUser", user);
        model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
        model.addAttribute("cartCount", cartService.getCartItemCount(userId));
        model.addAttribute("todayDiet", dietService.getTodayDiet().orElse(null));
        model.addAttribute("completedCount", fitnessService.getTodayCompletedFitnessIds(userId).size());
        return "user/dashboard";
    }
}
