package com.getfeetwithus.dto;

import java.math.BigDecimal;

public class SalesSummaryDto {
    private long totalOrders;
    private BigDecimal totalSales = BigDecimal.ZERO;
    private BigDecimal paidAmount = BigDecimal.ZERO;
    private BigDecimal pendingAmount = BigDecimal.ZERO;

    public SalesSummaryDto() {}

    public SalesSummaryDto(long totalOrders, BigDecimal totalSales, BigDecimal paidAmount, BigDecimal pendingAmount) {
        this.totalOrders = totalOrders;
        this.totalSales = totalSales != null ? totalSales : BigDecimal.ZERO;
        this.paidAmount = paidAmount != null ? paidAmount : BigDecimal.ZERO;
        this.pendingAmount = pendingAmount != null ? pendingAmount : BigDecimal.ZERO;
    }

    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
    public BigDecimal getTotalSales() { return totalSales; }
    public void setTotalSales(BigDecimal totalSales) { this.totalSales = totalSales; }
    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
    public BigDecimal getPendingAmount() { return pendingAmount; }
    public void setPendingAmount(BigDecimal pendingAmount) { this.pendingAmount = pendingAmount; }
}
