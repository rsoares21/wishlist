package com.wishlist.controller;

import com.wishlist.dto.AddProductRequest;
import com.wishlist.model.Wishlist;
import com.wishlist.service.WishlistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")

public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/{customerId}/add/{productId}")
    public Wishlist addProduct(@PathVariable String customerId, @PathVariable String productId) {
        return wishlistService.addProduct(customerId, productId);
    }

    @PostMapping("/add-product")
    public Wishlist addProduct(@RequestBody AddProductRequest request) {
        return wishlistService.addProduct(request.getCustomerId(), request.getProductId());
    }    

    @DeleteMapping("/{customerId}/remove/{productId}")
    public Wishlist removeProduct(@PathVariable String customerId, @PathVariable String productId) {
        return wishlistService.removeProduct(customerId, productId);
    }

    @GetMapping("/{customerId}")
    public Wishlist getWishlist(@PathVariable String customerId) {
        return wishlistService.getWishlist(customerId);
    }

    @GetMapping("/{customerId}/contains/{productId}")
    public boolean isProductInWishlist(@PathVariable String customerId, @PathVariable String productId) {
        return wishlistService.isProductInWishlist(customerId, productId);
    }
}
