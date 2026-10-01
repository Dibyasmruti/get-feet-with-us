package com.getfeetwithus.service;

import com.getfeetwithus.entity.Cart;
import com.getfeetwithus.entity.CartItem;
import com.getfeetwithus.entity.ProductMaster;
import com.getfeetwithus.repository.CartItemRepository;
import com.getfeetwithus.repository.CartRepository;
import com.getfeetwithus.repository.ProductMasterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductMasterRepository productMasterRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductMasterRepository productMasterRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productMasterRepository = productMasterRepository;
    }

    @Transactional
    public Cart getOrCreateCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    cart.setUpdatedAt(LocalDateTime.now());
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public void addToCart(String userId, String productId, int quantity) {
        if (quantity <= 0) quantity = 1;
        Cart cart = getOrCreateCart(userId);
        ProductMaster product = productMasterRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
        if (existingItemOpt.isPresent()) {
            CartItem item = existingItemOpt.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setProductPrice(product.getPrice());
            item.setIsSelected(true);
            cartItemRepository.save(item);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProductId(productId);
            item.setProductName(product.getName());
            item.setProductPrice(product.getPrice());
            item.setImageUrl(product.getImageUrl());
            item.setQuantity(quantity);
            item.setIsSelected(true);
            item.setCreatedAt(LocalDateTime.now());
            cartItemRepository.save(item);
        }
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Transactional
    public void updateQuantity(String userId, Long itemId, int delta) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found: " + itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Unauthorized cart item");
        }

        int newQty = item.getQuantity() + delta;
        if (newQty <= 0) {
            cart.removeItem(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(newQty);
            cartItemRepository.save(item);
        }
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Transactional
    public void removeItem(String userId, Long itemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found: " + itemId));

        if (item.getCart().getId().equals(cart.getId())) {
            cart.removeItem(item);
            cartItemRepository.delete(item);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
        }
    }

    @Transactional
    public void toggleSelection(String userId, Long itemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found: " + itemId));

        if (item.getCart().getId().equals(cart.getId())) {
            item.setIsSelected(!Boolean.TRUE.equals(item.getIsSelected()));
            cartItemRepository.save(item);
        }
    }

    public BigDecimal calculateSelectedTotal(Cart cart) {
        if (cart == null || cart.getItems() == null) {
            return BigDecimal.ZERO;
        }
        return cart.getItems().stream()
                .filter(i -> Boolean.TRUE.equals(i.getIsSelected()))
                .map(CartItem::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getCartItemCount(String userId) {
        Cart cart = getOrCreateCart(userId);
        return cart.getItems() != null ? cart.getItems().size() : 0;
    }
}
