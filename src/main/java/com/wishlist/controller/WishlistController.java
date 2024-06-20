package com.wishlist.controller;

import com.wishlist.controller.api.IWishlistController;
import com.wishlist.dto.AddProductRequest;
import com.wishlist.model.Wishlist;
import com.wishlist.service.impl.WishlistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")

public class WishlistController implements IWishlistController {

    @Autowired
    private WishlistService wishlistService;

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
        return ResponseEntity.status(HttpStatus.OK).body("This is a public message.");

    }
}
