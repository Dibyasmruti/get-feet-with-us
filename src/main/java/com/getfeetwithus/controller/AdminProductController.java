package com.getfeetwithus.controller;

import com.getfeetwithus.entity.ProductMaster;
import com.getfeetwithus.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/admin/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/admin/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new ProductMaster());
        return "admin/product_form";
    }

    @GetMapping("/admin/products/edit/{productId}")
    public String editProductForm(@PathVariable("productId") String productId, Model model) {
        ProductMaster product = productService.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        model.addAttribute("product", product);
        return "admin/product_form";
    }

    @PostMapping("/admin/products/save")
    public String saveProduct(@ModelAttribute("product") ProductMaster product,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {
        try {
            productService.saveProduct(product, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Product saved successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving product: " + ex.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products/toggle/{productId}")
    public String toggleProductStatus(@PathVariable("productId") String productId, RedirectAttributes redirectAttributes) {
        try {
            productService.toggleProductStatus(productId);
            redirectAttributes.addFlashAttribute("successMessage", "Product status updated.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products/toggle-avail/{productId}")
    public String toggleAvailability(@PathVariable("productId") String productId, RedirectAttributes redirectAttributes) {
        try {
            productService.toggleProductAvailability(productId);
            redirectAttributes.addFlashAttribute("successMessage", "Product availability updated.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/products";
    }
}
