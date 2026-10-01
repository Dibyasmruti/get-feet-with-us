package com.getfeetwithus.controller;

import com.getfeetwithus.dto.AdminChangePasswordDto;
import com.getfeetwithus.dto.UserChangePasswordDto;
import com.getfeetwithus.dto.UserForgotPasswordDto;
import com.getfeetwithus.dto.UserRegistrationDto;
import com.getfeetwithus.entity.User;
import com.getfeetwithus.service.AdminService;
import com.getfeetwithus.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;
    private final AdminService adminService;

    public AuthController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String root(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            return isAdmin ? "redirect:/admin/dashboard" : "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid User ID or Password");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        return "auth/login";
    }

    @GetMapping("/admin/login")
    public String adminLoginPage(@RequestParam(value = "error", required = false) String error,
                                 @RequestParam(value = "logout", required = false) String logout,
                                 Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid Admin ID or Password");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Admin logged out successfully.");
        }
        return "auth/admin_login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            User registered = userService.registerUser(userDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Registration successful! Your User ID is: " + registered.getUserId() + ". Please log in.");
            return "redirect:/login";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {
        model.addAttribute("forgotDto", new UserForgotPasswordDto());
        return "auth/forgot_password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@Valid @ModelAttribute("forgotDto") UserForgotPasswordDto forgotDto,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/forgot_password";
        }
        try {
            userService.resetPassword(forgotDto);
            redirectAttributes.addFlashAttribute("successMessage", "Password has been reset successfully. Please log in.");
            return "redirect:/login";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "auth/forgot_password";
        }
    }

    @GetMapping("/admin/forgot-password")
    public String adminForgotPasswordPage(Model model) {
        return "auth/admin_forgot_password";
    }

    @PostMapping("/admin/forgot-password")
    public String processAdminForgotPassword(@RequestParam("adminId") String adminId,
                                             @RequestParam("newPassword") String newPassword,
                                             @RequestParam("confirmPassword") String confirmPassword,
                                             RedirectAttributes redirectAttributes,
                                             Model model) {
        try {
            adminService.resetPassword(adminId, newPassword, confirmPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Admin password reset successfully. Please log in.");
            return "redirect:/admin/login";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "auth/admin_forgot_password";
        }
    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model model) {
        model.addAttribute("changeDto", new UserChangePasswordDto());
        return "auth/change_password";
    }

    @PostMapping("/change-password")
    public String processChangePassword(Authentication authentication,
                                        @Valid @ModelAttribute("changeDto") UserChangePasswordDto changeDto,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/change_password";
        }
        try {
            userService.changePassword(authentication.getName(), changeDto);
            redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully.");
            return "redirect:/dashboard";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "auth/change_password";
        }
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access_denied";
    }
}
