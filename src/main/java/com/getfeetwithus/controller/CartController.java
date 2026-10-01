package com.getfeetwithus.controller;

import com.getfeetwithus.entity.Cart;
import com.getfeetwithus.service.CartService;
import com.getfeetwithus.service.MeetingService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
public class CartController {

    private final CartService cartService;
    private final MeetingService meetingService;

    public CartController(CartService cartService, MeetingService meetingService) {
        this.cartService = cartService;
        this.meetingService = meetingService;
    }

    @GetMapping("/cart")
    public String viewCart(Authentication authentication, Model model) {
        String userId = authentication.getName();
        Cart cart = cartService.getOrCreateCart(userId);
        BigDecimal selectedTotal = cartService.calculateSelectedTotal(cart);

        model.addAttribute("cart", cart);
        model.addAttribute("selectedTotal", selectedTotal);
        model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
        model.addAttribute("cartCount", cartService.getCartItemCount(userId));
        return "user/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(Authentication authentication,
                            @RequestParam("productId") String productId,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                            RedirectAttributes redirectAttributes) {
        String userId = authentication.getName();
        cartService.addToCart(userId, productId, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Product added to cart!");
        return "redirect:/cart";
    }

    @PostMapping("/cart/update/{itemId}")
    public String updateQuantity(Authentication authentication,
                                 @PathVariable("itemId") Long itemId,
                                 @RequestParam("delta") int delta) {
        String userId = authentication.getName();
        cartService.updateQuantity(userId, itemId, delta);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{itemId}")
    public String removeItem(Authentication authentication,
                             @PathVariable("itemId") Long itemId,
                             RedirectAttributes redirectAttributes) {
        String userId = authentication.getName();
        cartService.removeItem(userId, itemId);
        redirectAttributes.addFlashAttribute("successMessage", "Item removed from cart.");
        return "redirect:/cart";
    }

    @PostMapping("/cart/toggle/{itemId}")
    public String toggleSelection(Authentication authentication,
                                  @PathVariable("itemId") Long itemId) {
        String userId = authentication.getName();
        cartService.toggleSelection(userId, itemId);
        return "redirect:/cart";
    }
}
