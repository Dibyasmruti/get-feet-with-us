package com.getfeetwithus.controller;

import com.getfeetwithus.dto.DietUploadDto;
import com.getfeetwithus.entity.DietMaster;
import com.getfeetwithus.service.CartService;
import com.getfeetwithus.service.DietService;
import com.getfeetwithus.service.MeetingService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class DietController {

    private final DietService dietService;
    private final MeetingService meetingService;
    private final CartService cartService;

    public DietController(DietService dietService, MeetingService meetingService, CartService cartService) {
        this.dietService = dietService;
        this.meetingService = meetingService;
        this.cartService = cartService;
    }

    @GetMapping("/diet")
    public String dietPage(Authentication authentication, Model model) {
        String userId = authentication.getName();
        Optional<DietMaster> dietOpt = dietService.getTodayDiet();
        model.addAttribute("diet", dietOpt.orElse(null));
        model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
        model.addAttribute("cartCount", cartService.getCartItemCount(userId));
        model.addAttribute("dietUploadDto", new DietUploadDto());
        model.addAttribute("userUploads", dietService.getUserUploads(userId));
        return "user/diet";
    }

    @PostMapping("/diet/upload")
    public String uploadTodayDiet(Authentication authentication,
                                  @ModelAttribute("dietUploadDto") DietUploadDto uploadDto,
                                  RedirectAttributes redirectAttributes) {
        try {
            dietService.saveUserDietUpload(authentication.getName(), uploadDto);
            redirectAttributes.addFlashAttribute("successMessage", "Today's diet photo/log uploaded successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Upload failed: " + ex.getMessage());
        }
        return "redirect:/diet";
    }
}
