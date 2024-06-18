package com.wishlist.repository;

import com.wishlist.model.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    Wishlist findByCustomerId(String customerId);
}