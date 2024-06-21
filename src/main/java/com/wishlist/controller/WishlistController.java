package com.wishlist.controller;

import com.wishlist.controller.api.WishlistApi;
import com.wishlist.dto.AddProductRequest;
import com.wishlist.model.Wishlist;
import com.wishlist.service.impl.WishlistServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WishlistController implements WishlistApi {

    @Autowired
    private WishlistServiceImpl wishlistService;

    @Override
    public ResponseEntity<Wishlist> addProduct(@PathVariable String customerId, @PathVariable String productId) {
        return ResponseEntity.ok(wishlistService.addProduct(customerId, productId));
    }

    @Override
    public ResponseEntity<Wishlist> addProductRequest(@RequestBody AddProductRequest request) {
        return ResponseEntity.ok(wishlistService.addProductRequest(request));
    }    

    @Override
    public ResponseEntity<Wishlist> removeProduct(@PathVariable String customerId, @PathVariable String productId) {
        return ResponseEntity.ok(wishlistService.removeProduct(customerId, productId));
    }

    @Override
    public ResponseEntity<Wishlist> getWishlist(@PathVariable String customerId) {
        return ResponseEntity.ok(wishlistService.getWishlist(customerId));
    }

    @Override
    public ResponseEntity<Boolean> isProductInWishlist(@PathVariable String customerId, @PathVariable String productId) {
        return ResponseEntity.ok(wishlistService.isProductInWishlist(customerId, productId));
    }
    
    @Override
    public ResponseEntity<String> publicApi() {
        return ResponseEntity.status(HttpStatus.OK).body("Esta é uma API pública");
    }
}
