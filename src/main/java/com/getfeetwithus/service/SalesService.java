package com.getfeetwithus.service;

import com.getfeetwithus.dto.SalesFilterDto;
import com.getfeetwithus.dto.SalesSummaryDto;
import com.getfeetwithus.entity.Order;
import com.getfeetwithus.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class SalesService {

    private final OrderRepository orderRepository;

    public SalesService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getSalesRecords(SalesFilterDto filter) {
        String type = filter.getFilterType() != null ? filter.getFilterType() : "THIS_MONTH";
        LocalDate today = LocalDate.now();

        switch (type.toUpperCase()) {
            case "TODAY": {
                LocalDateTime start = today.atStartOfDay();
                LocalDateTime end = today.atTime(LocalTime.MAX);
                return orderRepository.findOrdersBetweenDates(start, end);
            }
            case "THIS_MONTH": {
                YearMonth ym = YearMonth.now();
                LocalDateTime start = ym.atDay(1).atStartOfDay();
                LocalDateTime end = ym.atEndOfMonth().atTime(LocalTime.MAX);
                return orderRepository.findOrdersBetweenDates(start, end);
            }
            case "THIS_YEAR": {
                LocalDateTime start = LocalDate.of(today.getYear(), 1, 1).atStartOfDay();
                LocalDateTime end = LocalDate.of(today.getYear(), 12, 31).atTime(LocalTime.MAX);
                return orderRepository.findOrdersBetweenDates(start, end);
            }
            case "CUSTOM": {
                if (filter.getFromDate() != null && filter.getToDate() != null) {
                    LocalDateTime start = filter.getFromDate().atStartOfDay();
                    LocalDateTime end = filter.getToDate().atTime(LocalTime.MAX);
                    return orderRepository.findOrdersBetweenDates(start, end);
                }
                // Fallback to this month
                YearMonth ym = YearMonth.now();
                return orderRepository.findOrdersBetweenDates(ym.atDay(1).atStartOfDay(), ym.atEndOfMonth().atTime(LocalTime.MAX));
            }
            case "ALL":
            default:
                return orderRepository.findAllByOrderByOrderDateDesc();
        }
    }

    public SalesSummaryDto calculateSummary(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return new SalesSummaryDto(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        long totalOrders = orders.size();
        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal paidAmount = BigDecimal.ZERO;
        BigDecimal pendingAmount = BigDecimal.ZERO;

        for (Order o : orders) {
            BigDecimal amount = o.getGrandTotal() != null ? o.getGrandTotal() : BigDecimal.ZERO;
            totalSales = totalSales.add(amount);
            if ("Paid".equalsIgnoreCase(o.getPaymentStatus())) {
                paidAmount = paidAmount.add(amount);
            } else {
                pendingAmount = pendingAmount.add(amount);
            }
        }

        return new SalesSummaryDto(totalOrders, totalSales, paidAmount, pendingAmount);
    }
}
