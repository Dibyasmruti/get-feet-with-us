package com.getfeetwithus.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_master")
public class ProductMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", unique = true, nullable = false, length = 50)
    private String productId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "why_to_use", columnDefinition = "TEXT")
    private String whyToUse;

    @Column(name = "who_can_use", columnDefinition = "TEXT")
    private String whoCanUse;

    @Column(name = "how_to_use", columnDefinition = "TEXT")
    private String howToUse;

    @Column(name = "other_instructions", columnDefinition = "TEXT")
    private String otherInstructions;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public ProductMaster() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getWhyToUse() { return whyToUse; }
    public void setWhyToUse(String whyToUse) { this.whyToUse = whyToUse; }
    public String getWhoCanUse() { return whoCanUse; }
    public void setWhoCanUse(String whoCanUse) { this.whoCanUse = whoCanUse; }
    public String getHowToUse() { return howToUse; }
    public void setHowToUse(String howToUse) { this.howToUse = howToUse; }
    public String getOtherInstructions() { return otherInstructions; }
    public void setOtherInstructions(String otherInstructions) { this.otherInstructions = otherInstructions; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
