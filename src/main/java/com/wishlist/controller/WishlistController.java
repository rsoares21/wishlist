package com.wishlist.controller;

import com.wishlist.dto.AddProductRequest;
import com.wishlist.model.Wishlist;
import com.wishlist.service.WishlistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")

public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PreAuthorize("hasRole('users')")
    @PostMapping("/{customerId}/add/{productId}")
    public Wishlist addProduct(@PathVariable String customerId, @PathVariable String productId) {
        return wishlistService.addProduct(customerId, productId);
    }

    @PreAuthorize("hasRole('vip')")
    @PostMapping("/add-product")
    public Wishlist addProduct(@RequestBody AddProductRequest request) {
        return wishlistService.addProduct(request.getCustomerId(), request.getProductId());
    }    

    @PreAuthorize("hasRole('users')")
    @DeleteMapping("/{customerId}/remove/{productId}")
    public Wishlist removeProduct(@PathVariable String customerId, @PathVariable String productId) {
        return wishlistService.removeProduct(customerId, productId);
    }

    @PreAuthorize("hasAnyRole('default-roles-lab','users')")
    @GetMapping("/{customerId}")
    public Wishlist getWishlist(@PathVariable String customerId) {
        return wishlistService.getWishlist(customerId);
    }

    @PreAuthorize("hasRole('users')")
    @GetMapping("/{customerId}/contains/{productId}")
    public boolean isProductInWishlist(@PathVariable String customerId, @PathVariable String productId) {
        return wishlistService.isProductInWishlist(customerId, productId);
    }
    
    @GetMapping("/public")
    public ResponseEntity<String> publicApi() {
        return ResponseEntity.status(HttpStatus.OK).body("This is a public message.");

    }
}
