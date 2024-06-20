package com.wishlist.service.impl;

import com.wishlist.dto.AddProductRequest;
import com.wishlist.exception.DuplicateProductException;
import com.wishlist.exception.FulllWishlistException;
import com.wishlist.model.Wishlist;
import com.wishlist.repository.WishlistRepository;
import com.wishlist.service.IWishlistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WishlistService implements IWishlistService {

    @Value("${wishlist.max-products}")
    int maxProducts;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Override
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

    @Override
    public Wishlist removeProduct(String customerId, String productId) {
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        if (wishlist != null) {
            wishlist.getProductIds().remove(productId);
            return wishlistRepository.save(wishlist);
        }
        return null;
    }

    @Override
    public Wishlist getWishlist(String customerId) {
        return wishlistRepository.findByCustomerId(customerId);
    }

    @Override
    public boolean isProductInWishlist(String customerId, String productId) {
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        return wishlist != null && wishlist.getProductIds().contains(productId);
    }

    @Override
    public Wishlist addProductRequest(AddProductRequest request) {
        Wishlist wishlist = wishlistRepository.findByCustomerId(request.getCustomerId());

        //TO-DO: Criar mapper para converter AddProductRequest para Wishlist

        if (wishlist == null) {
            wishlist = new Wishlist();
            wishlist.setCustomerId(request.getCustomerId());
            wishlist.setProductIds(new ArrayList<>());
        }

        if (wishlist.getProductIds().contains(request.getProductId())) {
            throw new DuplicateProductException("Produto já existe na lista de desejos.");
        }

        if (wishlist.getProductIds().size() < maxProducts) {
            wishlist.getProductIds().add(request.getProductId());
            return wishlistRepository.save(wishlist);
        } else {
            throw new FulllWishlistException(String.format("Wishlist pode ter no máximo %d produtos.", maxProducts));
        }
    }
}
