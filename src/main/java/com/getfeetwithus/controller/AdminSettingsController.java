package com.getfeetwithus.controller;

import com.getfeetwithus.dto.AdminChangePasswordDto;
import com.getfeetwithus.entity.Admin;
import com.getfeetwithus.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminSettingsController {

    private final AdminService adminService;

    public AdminSettingsController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin/settings")
    public String adminSettings(Authentication authentication, Model model) {
        String adminId = authentication.getName();
        Admin admin = adminService.findByAdminId(adminId).orElse(null);
        List<Admin> allAdmins = adminService.getAllAdmins();

        model.addAttribute("currentAdmin", admin);
        model.addAttribute("allAdmins", allAdmins);
        model.addAttribute("changeDto", new AdminChangePasswordDto());

        return "admin/settings";
    }

    @PostMapping("/admin/settings/change-password")
    public String changeAdminPassword(Authentication authentication,
                                      @Valid @ModelAttribute("changeDto") AdminChangePasswordDto changeDto,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        String adminId = authentication.getName();
        if (bindingResult.hasErrors()) {
            Admin admin = adminService.findByAdminId(adminId).orElse(null);
            model.addAttribute("currentAdmin", admin);
            model.addAttribute("allAdmins", adminService.getAllAdmins());
            return "admin/settings";
        }

        try {
            adminService.changePassword(adminId, changeDto);
            redirectAttributes.addFlashAttribute("successMessage", "Admin password changed successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error changing password: " + ex.getMessage());
        }

        return "redirect:/admin/settings";
    }
}
