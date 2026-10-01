package com.getfeetwithus.controller;

import com.getfeetwithus.entity.OrganizationDetails;
import com.getfeetwithus.service.OrganizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminAboutController {

    private final OrganizationService organizationService;

    public AdminAboutController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/admin/about")
    public String adminAboutPage(Model model) {
        OrganizationDetails org = organizationService.getOrganizationDetails();
        model.addAttribute("org", org);
        return "admin/about";
    }

    @PostMapping("/admin/about/update")
    public String updateAboutInfo(@ModelAttribute("org") OrganizationDetails org,
                                  @RequestParam(value = "logoFile", required = false) MultipartFile logoFile,
                                  RedirectAttributes redirectAttributes) {
        try {
            organizationService.updateOrganizationDetails(org, logoFile);
            redirectAttributes.addFlashAttribute("successMessage", "Organization details updated successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating organization details: " + ex.getMessage());
        }
        return "redirect:/admin/about";
    }
}
