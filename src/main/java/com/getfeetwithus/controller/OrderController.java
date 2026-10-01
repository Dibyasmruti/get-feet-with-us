package com.getfeetwithus.controller;

import com.getfeetwithus.dto.OrderRequestDto;
import com.getfeetwithus.entity.Cart;
import com.getfeetwithus.entity.CartItem;
import com.getfeetwithus.entity.Order;
import com.getfeetwithus.entity.OrganizationDetails;
import com.getfeetwithus.entity.User;
import com.getfeetwithus.entity.UserAddress;
import com.getfeetwithus.repository.UserAddressRepository;
import com.getfeetwithus.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;
    private final UserAddressRepository userAddressRepository;
    private final MeetingService meetingService;
    private final OrganizationService organizationService;
    private final BillPdfService billPdfService;

    public OrderController(OrderService orderService,
                           CartService cartService,
                           UserService userService,
                           UserAddressRepository userAddressRepository,
                           MeetingService meetingService,
                           OrganizationService organizationService,
                           BillPdfService billPdfService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.userService = userService;
        this.userAddressRepository = userAddressRepository;
        this.meetingService = meetingService;
        this.organizationService = organizationService;
        this.billPdfService = billPdfService;
    }

    @GetMapping("/order/preview")
    public String orderPreview(Authentication authentication, Model model) {
        String userId = authentication.getName();
        User user = userService.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = cartService.getOrCreateCart(userId);
        List<CartItem> selectedItems = cart.getItems().stream()
                .filter(i -> Boolean.TRUE.equals(i.getIsSelected()) && i.getQuantity() > 0)
                .collect(Collectors.toList());

        if (selectedItems.isEmpty()) {
            return "redirect:/cart";
        }

        BigDecimal grandTotal = cartService.calculateSelectedTotal(cart);

        OrderRequestDto orderDto = new OrderRequestDto();
        orderDto.setCustomerName(user.getName());
        orderDto.setMobileNumber(user.getMobileNumber());

        Optional<UserAddress> defAddress = userAddressRepository.findByUserIdAndIsDefaultTrue(userId);
        if (defAddress.isPresent()) {
            UserAddress addr = defAddress.get();
            orderDto.setAddressLine(addr.getAddressLine());
            orderDto.setAtLocality(addr.getAtLocality());
            orderDto.setPostOffice(addr.getPostOffice());
            orderDto.setDistrict(addr.getDistrict());
            orderDto.setState(addr.getState());
            orderDto.setPinCode(addr.getPinCode());
            orderDto.setNearbyPlace(addr.getNearbyPlace());
        } else {
            orderDto.setAddressLine(user.getAddress());
            orderDto.setPinCode(user.getPinCode());
        }

        model.addAttribute("selectedItems", selectedItems);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("orderDto", orderDto);
        model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
        model.addAttribute("cartCount", cartService.getCartItemCount(userId));

        return "user/order_preview";
    }

    @PostMapping("/order/confirm")
    public String confirmOrder(Authentication authentication,
                               @Valid @ModelAttribute("orderDto") OrderRequestDto orderDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        String userId = authentication.getName();
        if (bindingResult.hasErrors()) {
            Cart cart = cartService.getOrCreateCart(userId);
            List<CartItem> selectedItems = cart.getItems().stream()
                    .filter(i -> Boolean.TRUE.equals(i.getIsSelected()) && i.getQuantity() > 0)
                    .collect(Collectors.toList());
            model.addAttribute("selectedItems", selectedItems);
            model.addAttribute("grandTotal", cartService.calculateSelectedTotal(cart));
            model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
            model.addAttribute("cartCount", cartService.getCartItemCount(userId));
            return "user/order_preview";
        }

        try {
            Order order = orderService.placeOrder(userId, orderDto);
            redirectAttributes.addFlashAttribute("successMessage", "Order #" + order.getOrderId() + " placed successfully!");
            return "redirect:/orders/" + order.getOrderId();
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not place order: " + ex.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/orders")
    public String userOrders(Authentication authentication, Model model) {
        String userId = authentication.getName();
        List<Order> orders = orderService.getUserOrders(userId);
        model.addAttribute("orders", orders);
        model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
        model.addAttribute("cartCount", cartService.getCartItemCount(userId));
        return "user/orders";
    }

    @GetMapping("/orders/{orderId}")
    public String orderDetails(@PathVariable("orderId") String orderId,
                               Authentication authentication,
                               Model model) {
        String userId = authentication.getName();
        Order order = orderService.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        if (!order.getUserId().equals(userId)) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
        model.addAttribute("cartCount", cartService.getCartItemCount(userId));
        return "user/order_details";
    }

    @GetMapping("/orders/{orderId}/bill")
    public String viewBill(@PathVariable("orderId") String orderId,
                           Authentication authentication,
                           Model model) {
        String userId = authentication.getName();
        Order order = orderService.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        if (!order.getUserId().equals(userId)) {
            return "redirect:/orders";
        }

        OrganizationDetails org = organizationService.getOrganizationDetails();
        model.addAttribute("order", order);
        model.addAttribute("org", org);
        return "user/bill";
    }

    @GetMapping("/orders/{orderId}/bill/download")
    public ResponseEntity<byte[]> downloadBill(@PathVariable("orderId") String orderId,
                                               Authentication authentication) throws Exception {
        String userId = authentication.getName();
        Order order = orderService.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        if (!order.getUserId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        OrganizationDetails org = organizationService.getOrganizationDetails();
        byte[] pdfBytes = billPdfService.generateBillPdf(order, org);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Bill_" + order.getOrderId() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
