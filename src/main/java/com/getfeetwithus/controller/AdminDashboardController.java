package com.getfeetwithus.controller;

import com.getfeetwithus.dto.SalesFilterDto;
import com.getfeetwithus.dto.SalesSummaryDto;
import com.getfeetwithus.entity.Order;
import com.getfeetwithus.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminDashboardController {

    private final UserService userService;
    private final DietService dietService;
    private final FitnessService fitnessService;
    private final ProductService productService;
    private final OrderService orderService;
    private final MeetingService meetingService;
    private final SalesService salesService;

    public AdminDashboardController(UserService userService,
                                    DietService dietService,
                                    FitnessService fitnessService,
                                    ProductService productService,
                                    OrderService orderService,
                                    MeetingService meetingService,
                                    SalesService salesService) {
        this.userService = userService;
        this.dietService = dietService;
        this.fitnessService = fitnessService;
        this.productService = productService;
        this.orderService = orderService;
        this.meetingService = meetingService;
        this.salesService = salesService;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {
        String adminId = authentication.getName();
        model.addAttribute("adminId", adminId);

        // Stats
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalDietPlans", dietService.getAllDietPlans().size());
        model.addAttribute("totalActivities", fitnessService.getAllActivities().size());
        model.addAttribute("totalProducts", productService.getAllProducts().size());
        model.addAttribute("totalOrders", orderService.getAllOrders().size());
        model.addAttribute("todayMeetings", meetingService.getTodayMeetings().size());

        // This Month Sales Summary
        SalesFilterDto thisMonthFilter = new SalesFilterDto();
        thisMonthFilter.setFilterType("THIS_MONTH");
        List<Order> thisMonthOrders = salesService.getSalesRecords(thisMonthFilter);
        SalesSummaryDto salesSummary = salesService.calculateSummary(thisMonthOrders);
        model.addAttribute("salesSummary", salesSummary);

        // Recent Orders
        List<Order> recentOrders = orderService.getAllOrders();
        if (recentOrders.size() > 5) {
            recentOrders = recentOrders.subList(0, 5);
        }
        model.addAttribute("recentOrders", recentOrders);

        return "admin/dashboard";
    }
}
