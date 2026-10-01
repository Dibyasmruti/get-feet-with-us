package com.getfeetwithus.controller;

import com.getfeetwithus.entity.DietMaster;
import com.getfeetwithus.service.DietService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class AdminDietController {

    private final DietService dietService;

    public AdminDietController(DietService dietService) {
        this.dietService = dietService;
    }

    @GetMapping("/admin/diet")
    public String listDietPlans(Model model) {
        model.addAttribute("dietPlans", dietService.getAllDietPlans());
        model.addAttribute("userUploads", dietService.getAllUserUploads());
        return "admin/diet";
    }

    @GetMapping("/admin/diet/new")
    public String newDietForm(Model model) {
        DietMaster diet = new DietMaster();
        diet.setDietDate(LocalDate.now());
        model.addAttribute("diet", diet);
        return "admin/diet_form";
    }

    @GetMapping("/admin/diet/edit/{dietId}")
    public String editDietForm(@PathVariable("dietId") String dietId, Model model) {
        DietMaster diet = dietService.findByDietId(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Diet plan not found: " + dietId));
        model.addAttribute("diet", diet);
        return "admin/diet_form";
    }

    @PostMapping("/admin/diet/save")
    public String saveDiet(@ModelAttribute("diet") DietMaster diet,
                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        try {
            dietService.saveDietPlan(diet, imageFile, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Diet plan saved successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving diet plan: " + ex.getMessage());
        }
        return "redirect:/admin/diet";
    }

    @PostMapping("/admin/diet/toggle/{dietId}")
    public String toggleDietStatus(@PathVariable("dietId") String dietId, RedirectAttributes redirectAttributes) {
        try {
            dietService.toggleDietStatus(dietId);
            redirectAttributes.addFlashAttribute("successMessage", "Diet plan status updated.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/diet";
    }
}
