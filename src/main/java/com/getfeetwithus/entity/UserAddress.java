package com.getfeetwithus.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_addresses")
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "address_line", columnDefinition = "TEXT", nullable = false)
    private String addressLine;

    @Column(name = "at_locality", length = 150)
    private String atLocality;

    @Column(name = "post_office", length = 100)
    private String postOffice;

    @Column(length = 100)
    private String district;

    @Column(length = 100)
    private String state;

    @Column(name = "pin_code", nullable = false, length = 10)
    private String pinCode;

    @Column(name = "nearby_place", length = 150)
    private String nearbyPlace;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public UserAddress() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
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
    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
