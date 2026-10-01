package com.getfeetwithus.controller;

import com.getfeetwithus.entity.User;
import com.getfeetwithus.service.DietService;
import com.getfeetwithus.service.FitnessService;
import com.getfeetwithus.service.OrderService;
import com.getfeetwithus.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminUserController {

    private final UserService userService;
    private final DietService dietService;
    private final FitnessService fitnessService;
    private final OrderService orderService;

    public AdminUserController(UserService userService,
                               DietService dietService,
                               FitnessService fitnessService,
                               OrderService orderService) {
        this.userService = userService;
        this.dietService = dietService;
        this.fitnessService = fitnessService;
        this.orderService = orderService;
    }

    @GetMapping("/admin/users")
    public String listUsers(@RequestParam(value = "search", required = false) String search, Model model) {
        List<User> users;
        if (search != null && !search.isBlank()) {
            users = userService.searchUsers(search);
            model.addAttribute("search", search);
        } else {
            users = userService.getAllUsers();
        }
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/admin/users/{userId}")
    public String userDetails(@PathVariable("userId") String userId, Model model) {
        User user = userService.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        model.addAttribute("user", user);
        model.addAttribute("userDietUploads", dietService.getUserUploads(userId));
        model.addAttribute("userCompletions", fitnessService.getUserCompletions(userId));
        model.addAttribute("userOrders", orderService.getUserOrders(userId));

        return "admin/user_details";
    }

    @PostMapping("/admin/users/{userId}/toggle")
    public String toggleUserStatus(@PathVariable("userId") String userId, RedirectAttributes redirectAttributes) {
        try {
            userService.toggleUserStatus(userId);
            redirectAttributes.addFlashAttribute("successMessage", "User status updated successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/users";
    }
}
