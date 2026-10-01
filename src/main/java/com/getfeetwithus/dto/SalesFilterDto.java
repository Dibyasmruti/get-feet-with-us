package com.getfeetwithus.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class SalesFilterDto {
    private String filterType = "THIS_MONTH"; // ALL, THIS_YEAR, THIS_MONTH, TODAY, CUSTOM

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate toDate;

    public SalesFilterDto() {}

    public String getFilterType() { return filterType; }
    public void setFilterType(String filterType) { this.filterType = filterType; }
    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }
    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }
}
