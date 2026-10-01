package com.getfeetwithus.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_addresses")
public class OrderAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_db_id", nullable = false)
    private Order order;

    @Column(name = "customer_name", nullable = false, length = 150)
    private String customerName;

    @Column(name = "mobile_number", nullable = false, length = 20)
    private String mobileNumber;

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

    public OrderAddress() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
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

    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        if (addressLine != null && !addressLine.isBlank()) sb.append(addressLine);
        if (atLocality != null && !atLocality.isBlank()) sb.append(", At: ").append(atLocality);
        if (postOffice != null && !postOffice.isBlank()) sb.append(", PO: ").append(postOffice);
        if (district != null && !district.isBlank()) sb.append(", Dist: ").append(district);
        if (state != null && !state.isBlank()) sb.append(", ").append(state);
        if (pinCode != null && !pinCode.isBlank()) sb.append(" - ").append(pinCode);
        if (nearbyPlace != null && !nearbyPlace.isBlank()) sb.append(" (Near: ").append(nearbyPlace).append(")");
        return sb.toString();
    }
}
