package com.getfeetwithus.service;

import com.getfeetwithus.dto.OrderRequestDto;
import com.getfeetwithus.entity.*;
import com.getfeetwithus.repository.CartItemRepository;
import com.getfeetwithus.repository.CartRepository;
import com.getfeetwithus.repository.OrderRepository;
import com.getfeetwithus.repository.UserRepository;
import com.getfeetwithus.util.IdGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final IdGeneratorUtil idGeneratorUtil;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        UserRepository userRepository,
                        IdGeneratorUtil idGeneratorUtil) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.idGeneratorUtil = idGeneratorUtil;
    }

    @Transactional
    public Order placeOrder(String userId, OrderRequestDto addressDto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + userId));

        List<CartItem> selectedItems = cart.getItems().stream()
                .filter(i -> Boolean.TRUE.equals(i.getIsSelected()) && i.getQuantity() > 0)
                .collect(Collectors.toList());

        if (selectedItems.isEmpty()) {
            throw new IllegalArgumentException("No products selected for ordering.");
        }

        BigDecimal grandTotal = selectedItems.stream()
                .map(CartItem::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderId(idGeneratorUtil.generateOrderId());
        order.setUserId(userId);
        order.setUserName(user.getName());
        order.setUserMobile(user.getMobileNumber());
        order.setOrderDate(LocalDateTime.now());
        order.setGrandTotal(grandTotal);
        order.setOrderStatus("Order Placed");
        order.setPaymentStatus("Pending");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Copy item snapshots
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem ci : selectedItems) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProductId(ci.getProductId());
            oi.setProductName(ci.getProductName());
            oi.setProductImage(ci.getImageUrl());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getProductPrice());
            oi.setProductTotal(ci.getItemTotal());
            orderItems.add(oi);
        }
        order.setItems(orderItems);

        // Copy delivery address snapshot
        OrderAddress address = new OrderAddress();
        address.setOrder(order);
        address.setCustomerName(addressDto.getCustomerName().trim());
        address.setMobileNumber(addressDto.getMobileNumber().trim());
        address.setAddressLine(addressDto.getAddressLine().trim());
        address.setAtLocality(addressDto.getAtLocality() != null ? addressDto.getAtLocality().trim() : "");
        address.setPostOffice(addressDto.getPostOffice() != null ? addressDto.getPostOffice().trim() : "");
        address.setDistrict(addressDto.getDistrict() != null ? addressDto.getDistrict().trim() : "");
        address.setState(addressDto.getState() != null ? addressDto.getState().trim() : "");
        address.setPinCode(addressDto.getPinCode().trim());
        address.setNearbyPlace(addressDto.getNearbyPlace() != null ? addressDto.getNearbyPlace().trim() : "");
        order.setDeliveryAddress(address);

        Order savedOrder = orderRepository.save(order);

        // Remove selected items from cart, keep unselected items
        for (CartItem ci : selectedItems) {
            cart.removeItem(ci);
            cartItemRepository.delete(ci);
        }
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        return savedOrder;
    }

    public List<Order> getUserOrders(String userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    public Optional<Order> findByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    @Transactional
    public void updateOrderStatus(String orderId, String newStatus) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        order.setOrderStatus(newStatus);
        if ("Order Delivered".equalsIgnoreCase(newStatus)) {
            order.setDeliveredAt(LocalDateTime.now());
        }
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Transactional
    public void updatePaymentStatus(String orderId, String paymentStatus) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        order.setPaymentStatus(paymentStatus);
        if ("Paid".equalsIgnoreCase(paymentStatus)) {
            order.setPaymentUpdatedAt(LocalDateTime.now());
        } else {
            order.setPaymentUpdatedAt(null);
        }
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }
}
