package com.wishlist.dto;

public class AddProductRequest {
    
    private String customerId;
    private String productId;

    // Getters and setters (required for Jackson to deserialize JSON)
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
