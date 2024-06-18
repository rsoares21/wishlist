package com.wishlist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddProductRequest {
    
    @NotBlank(message = "customerId é obrigatório")
    private String customerId;
    @NotBlank(message = "productId é obrigatório")
    private String productId;

}
