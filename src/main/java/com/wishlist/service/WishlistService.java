package com.wishlist.service;

import com.wishlist.dto.AddProductRequest;
import com.wishlist.model.Wishlist;

public interface WishlistService {

    Wishlist addProduct(String customerId, String productId);

    Wishlist addProductRequest(AddProductRequest request);

    Wishlist removeProduct(String customerId, String productId);

    Wishlist getWishlist(String customerId);

    boolean isProductInWishlist(String customerId, String productId);

}
