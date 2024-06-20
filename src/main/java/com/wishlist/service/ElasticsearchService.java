package com.wishlist.service;

import java.io.IOException;

import com.wishlist.dto.AddProductRequest;
import com.wishlist.model.Wishlist;

public interface ElasticsearchService {
    void indexWishlistAddProduct(AddProductRequest requestPayload, Wishlist responsePayload) throws IOException;
}