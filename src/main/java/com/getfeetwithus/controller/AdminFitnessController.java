package com.getfeetwithus.controller;

import com.getfeetwithus.entity.FitnessMaster;
import com.getfeetwithus.service.FitnessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminFitnessController {

    private final FitnessService fitnessService;

    public AdminFitnessController(FitnessService fitnessService) {
        this.fitnessService = fitnessService;
    }

    @GetMapping("/admin/fitness")
    public String listActivities(Model model) {
        model.addAttribute("activities", fitnessService.getAllActivities());
        model.addAttribute("completions", fitnessService.getAllCompletions());
        return "admin/fitness";
    }

    @GetMapping("/admin/fitness/new")
    public String newActivityForm(Model model) {
        model.addAttribute("activity", new FitnessMaster());
        return "admin/fitness_form";
    }

    @GetMapping("/admin/fitness/edit/{fitnessId}")
    public String editActivityForm(@PathVariable("fitnessId") String fitnessId, Model model) {
        FitnessMaster activity = fitnessService.findByFitnessId(fitnessId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found: " + fitnessId));
        model.addAttribute("activity", activity);
        return "admin/fitness_form";
    }

    @PostMapping("/admin/fitness/save")
    public String saveActivity(@ModelAttribute("activity") FitnessMaster activity,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               RedirectAttributes redirectAttributes) {
        try {
            fitnessService.saveActivity(activity, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Fitness activity saved successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving activity: " + ex.getMessage());
        }
        return "redirect:/admin/fitness";
    }

    @PostMapping("/admin/fitness/toggle/{fitnessId}")
    public String toggleActivityStatus(@PathVariable("fitnessId") String fitnessId, RedirectAttributes redirectAttributes) {
        try {
            fitnessService.toggleActivityStatus(fitnessId);
            redirectAttributes.addFlashAttribute("successMessage", "Activity status updated.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/fitness";
    }
}
