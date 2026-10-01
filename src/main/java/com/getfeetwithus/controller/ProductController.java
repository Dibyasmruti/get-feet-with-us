package com.getfeetwithus.controller;

import com.getfeetwithus.entity.ProductMaster;
import com.getfeetwithus.service.CartService;
import com.getfeetwithus.service.MeetingService;
import com.getfeetwithus.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final MeetingService meetingService;
    private final CartService cartService;

    public ProductController(ProductService productService, MeetingService meetingService, CartService cartService) {
        this.productService = productService;
        this.meetingService = meetingService;
        this.cartService = cartService;
    }

    @GetMapping("/products")
    public String productsList(Authentication authentication, Model model) {
        String userId = authentication.getName();
        List<ProductMaster> products = productService.getAvailableProducts();
        model.addAttribute("products", products);
        model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
        model.addAttribute("cartCount", cartService.getCartItemCount(userId));
        return "user/products";
    }

    @GetMapping("/products/{productId}")
    public String productDetails(@PathVariable("productId") String productId,
                                 Authentication authentication,
                                 Model model) {
        String userId = authentication.getName();
        ProductMaster product = productService.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        model.addAttribute("product", product);
        model.addAttribute("hasTodayMeeting", meetingService.hasTodayMeeting());
        model.addAttribute("cartCount", cartService.getCartItemCount(userId));
        return "user/product_details";
    }
}
