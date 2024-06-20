package com.wishlist.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.wishlist.dto.AddProductRequest;
import com.wishlist.model.Wishlist;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

public interface IWishlistController {

    @Operation(summary = "Add product to wishlist by customer and product IDs")
    @PreAuthorize("hasRole('users')")
    @PostMapping("/{customerId}/add/{productId}")
    ResponseEntity<Wishlist> addProduct(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @Parameter(description = "Product ID") @PathVariable String productId);

    @Operation(summary = "Add product to wishlist using request body")
    @PreAuthorize("hasRole('vip')")
    @PostMapping("/add-product")
    ResponseEntity<Wishlist> addProductRequest(
            @RequestBody AddProductRequest request);

    @Operation(summary = "Remove product from wishlist by customer and product IDs")
    @PreAuthorize("hasRole('users')")
    @DeleteMapping("/{customerId}/remove/{productId}")
    ResponseEntity<Wishlist> removeProduct(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @Parameter(description = "Product ID") @PathVariable String productId);

    @Operation(summary = "Get wishlist by customer ID")
    @PreAuthorize("hasAnyRole('default-roles-lab','users')")
    @GetMapping("/{customerId}")
    ResponseEntity<Wishlist> getWishlist(
            @Parameter(description = "Customer ID") @PathVariable String customerId);

    @Operation(summary = "Check if product is in wishlist by customer and product IDs")
    @PreAuthorize("hasRole('users')")
    @GetMapping("/{customerId}/contains/{productId}")
    ResponseEntity<Boolean> isProductInWishlist(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @Parameter(description = "Product ID") @PathVariable String productId);

    @Operation(summary = "Public API endpoint")
    @GetMapping("/public")
    ResponseEntity<String> publicApi();
}
