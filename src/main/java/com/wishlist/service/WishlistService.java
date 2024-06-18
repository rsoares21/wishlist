package com.wishlist.service;

import com.wishlist.exception.DuplicateProductException;
import com.wishlist.exception.FulllWishlistException;
import com.wishlist.model.Wishlist;
import com.wishlist.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WishlistService {

    @Value("${wishlist.max-products}")
    int maxProducts;

    @Autowired
    private WishlistRepository wishlistRepository;

    public Wishlist addProduct(String customerId, String productId) {
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);

        if (wishlist == null) {
            wishlist = new Wishlist();
            wishlist.setCustomerId(customerId);
            wishlist.setProductIds(new ArrayList<>());
        }

        if (wishlist.getProductIds().contains(productId)) {
            throw new DuplicateProductException("Produto já existe na lista de desejos.");
        }

        if (wishlist.getProductIds().size() < maxProducts) {
            wishlist.getProductIds().add(productId);
            return wishlistRepository.save(wishlist);
        } else {
            throw new FulllWishlistException(String.format("Wishlist pode ter no máximo %d produtos.", maxProducts));
        }
    }

    public Wishlist removeProduct(String customerId, String productId) {
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        if (wishlist != null) {
            wishlist.getProductIds().remove(productId);
            return wishlistRepository.save(wishlist);
        }
        return null;
    }

    public Wishlist getWishlist(String customerId) {
        return wishlistRepository.findByCustomerId(customerId);
    }

    public boolean isProductInWishlist(String customerId, String productId) {
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        return wishlist != null && wishlist.getProductIds().contains(productId);
    }
}
