package com.getfeetwithus.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "organization_details")
public class OrganizationDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "org_name", nullable = false, length = 200)
    private String orgName = "Get Feet With Us";

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "about_info", columnDefinition = "TEXT")
    private String aboutInfo;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;

    @Column(length = 100)
    private String email;

    @Column(length = 150)
    private String website;

    @Column(name = "other_info", columnDefinition = "TEXT")
    private String otherInfo;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public OrganizationDetails() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getAboutInfo() { return aboutInfo; }
    public void setAboutInfo(String aboutInfo) { this.aboutInfo = aboutInfo; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getOtherInfo() { return otherInfo; }
    public void setOtherInfo(String otherInfo) { this.otherInfo = otherInfo; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
