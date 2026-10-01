package com.getfeetwithus.controller;

import com.getfeetwithus.entity.Order;
import com.getfeetwithus.entity.OrganizationDetails;
import com.getfeetwithus.service.BillPdfService;
import com.getfeetwithus.service.OrderService;
import com.getfeetwithus.service.OrganizationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminOrderController {

    private final OrderService orderService;
    private final OrganizationService organizationService;
    private final BillPdfService billPdfService;

    public AdminOrderController(OrderService orderService,
                                OrganizationService organizationService,
                                BillPdfService billPdfService) {
        this.orderService = orderService;
        this.organizationService = organizationService;
        this.billPdfService = billPdfService;
    }

    @GetMapping("/admin/orders")
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin/orders";
    }

    @GetMapping("/admin/orders/{orderId}")
    public String orderDetails(@PathVariable("orderId") String orderId, Model model) {
        Order order = orderService.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        model.addAttribute("order", order);
        return "admin/order_details";
    }

    @PostMapping("/admin/orders/{orderId}/status")
    public String updateOrderStatus(@PathVariable("orderId") String orderId,
                                    @RequestParam("status") String status,
                                    RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(orderId, status);
            redirectAttributes.addFlashAttribute("successMessage", "Order status updated to: " + status);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order status: " + ex.getMessage());
        }
        return "redirect:/admin/orders/" + orderId;
    }

    @PostMapping("/admin/orders/{orderId}/payment")
    public String updatePaymentStatus(@PathVariable("orderId") String orderId,
                                      @RequestParam("paymentStatus") String paymentStatus,
                                      RedirectAttributes redirectAttributes) {
        try {
            orderService.updatePaymentStatus(orderId, paymentStatus);
            redirectAttributes.addFlashAttribute("successMessage", "Payment status updated to: " + paymentStatus);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating payment status: " + ex.getMessage());
        }
        return "redirect:/admin/orders/" + orderId;
    }

    @GetMapping("/admin/orders/{orderId}/bill")
    public String viewBill(@PathVariable("orderId") String orderId, Model model) {
        Order order = orderService.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        OrganizationDetails org = organizationService.getOrganizationDetails();
        model.addAttribute("order", order);
        model.addAttribute("org", org);
        return "user/bill";
    }

    @GetMapping("/admin/orders/{orderId}/bill/download")
    public ResponseEntity<byte[]> downloadBill(@PathVariable("orderId") String orderId) throws Exception {
        Order order = orderService.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        OrganizationDetails org = organizationService.getOrganizationDetails();
        byte[] pdfBytes = billPdfService.generateBillPdf(order, org);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Bill_" + order.getOrderId() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
