package com.getfeetwithus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OrderRequestDto {
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message = "Address line is required")
    private String addressLine;

    private String atLocality;
    private String postOffice;
    private String district;
    private String state;

    @NotBlank(message = "PIN code is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "PIN code must be 6 digits")
    private String pinCode;

    private String nearbyPlace;

    public OrderRequestDto() {}

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }
    public String getAtLocality() { return atLocality; }
    public void setAtLocality(String atLocality) { this.atLocality = atLocality; }
    public String getPostOffice() { return postOffice; }
    public void setPostOffice(String postOffice) { this.postOffice = postOffice; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getPinCode() { return pinCode; }
    public void setPinCode(String pinCode) { this.pinCode = pinCode; }
    public String getNearbyPlace() { return nearbyPlace; }
    public void setNearbyPlace(String nearbyPlace) { this.nearbyPlace = nearbyPlace; }
}
