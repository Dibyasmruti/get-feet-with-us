package com.getfeetwithus.controller;

import com.getfeetwithus.entity.FitnessMaster;
import com.getfeetwithus.service.CartService;
import com.getfeetwithus.service.FitnessService;
import com.getfeetwithus.service.MeetingService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
public class FitnessController {

    private final FitnessService fitnessService;
    private final MeetingService meetingService;
    private final CartService cartService;

    public FitnessController(FitnessService fitnessService, MeetingService meetingService, CartService cartService) {
        this.fitnessService = fitnessService;
        this.meetingService = meetingService;
        this.cartService = cartService;
    }

    @GetMapping("/fitness")
    public String fitnessPage(Authentication authentication, Model model) {
        String userId = authentication.getName();
        List<FitnessMaster> activities = fitnessService.getActiveActivities();
        Set<String> completedIds = fitnessService.getTodayCompletedFitnessIds(userId);

        model.addAttribute("activities", activities);
        model.addAttribute("completedIds", completedIds);
        model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
        model.addAttribute("cartCount", cartService.getCartItemCount(userId));
        model.addAttribute("completionHistory", fitnessService.getUserCompletions(userId));

        return "user/fitness";
    }

    @PostMapping("/fitness/complete/{fitnessId}")
    public String markComplete(Authentication authentication,
                               @PathVariable("fitnessId") String fitnessId,
                               RedirectAttributes redirectAttributes) {
        String userId = authentication.getName();
        boolean completed = fitnessService.markAsComplete(userId, fitnessId);
        if (completed) {
            redirectAttributes.addFlashAttribute("successMessage", "Great job! Activity marked as complete for today.");
        } else {
            redirectAttributes.addFlashAttribute("infoMessage", "You have already completed this activity today.");
        }
        return "redirect:/fitness";
    }
}
