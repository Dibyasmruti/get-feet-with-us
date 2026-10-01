package com.getfeetwithus.controller;

import com.getfeetwithus.dto.UserSettingsDto;
import com.getfeetwithus.entity.User;
import com.getfeetwithus.service.CartService;
import com.getfeetwithus.service.MeetingService;
import com.getfeetwithus.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingsController {

    private final UserService userService;
    private final MeetingService meetingService;
    private final CartService cartService;

    public SettingsController(UserService userService, MeetingService meetingService, CartService cartService) {
        this.userService = userService;
        this.meetingService = meetingService;
        this.cartService = cartService;
    }

    @GetMapping("/settings")
    public String settingsPage(Authentication authentication, Model model) {
        String userId = authentication.getName();
        User user = userService.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        UserSettingsDto settingsDto = new UserSettingsDto();
        settingsDto.setUserId(user.getUserId());
        settingsDto.setName(user.getName());
        settingsDto.setEmail(user.getEmail());
        settingsDto.setMobileNumber(user.getMobileNumber());
        settingsDto.setAddress(user.getAddress());
        settingsDto.setPinCode(user.getPinCode());

        model.addAttribute("settingsDto", settingsDto);
        model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
        model.addAttribute("cartCount", cartService.getCartItemCount(userId));
        return "user/settings";
    }

    @PostMapping("/settings/update")
    public String updateSettings(Authentication authentication,
                                 @Valid @ModelAttribute("settingsDto") UserSettingsDto settingsDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        String userId = authentication.getName();
        if (bindingResult.hasErrors()) {
            User user = userService.findByUserId(userId).orElse(null);
            if (user != null) {
                settingsDto.setUserId(user.getUserId());
                settingsDto.setName(user.getName());
                settingsDto.setEmail(user.getEmail());
                settingsDto.setMobileNumber(user.getMobileNumber());
            }
            model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
            model.addAttribute("cartCount", cartService.getCartItemCount(userId));
            return "user/settings";
        }

        try {
            userService.updateUserSettings(userId, settingsDto);
            redirectAttributes.addFlashAttribute("successMessage", "Address and PIN code updated successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update settings: " + ex.getMessage());
        }

        return "redirect:/settings";
    }
}
