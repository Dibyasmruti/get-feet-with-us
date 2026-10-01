package com.getfeetwithus.controller;

import com.getfeetwithus.dto.SalesFilterDto;
import com.getfeetwithus.dto.SalesSummaryDto;
import com.getfeetwithus.entity.Order;
import com.getfeetwithus.service.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class AdminSalesController {

    private final SalesService salesService;

    public AdminSalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/admin/sales")
    public String salesReport(@ModelAttribute("filter") SalesFilterDto filter, Model model) {
        if (filter.getFilterType() == null || filter.getFilterType().isBlank()) {
            filter.setFilterType("THIS_MONTH");
        }

        List<Order> orders = salesService.getSalesRecords(filter);
        SalesSummaryDto summary = salesService.calculateSummary(orders);

        model.addAttribute("orders", orders);
        model.addAttribute("summary", summary);
        model.addAttribute("filter", filter);

        return "admin/sales";
    }
}
