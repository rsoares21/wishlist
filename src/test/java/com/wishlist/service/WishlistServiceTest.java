package com.wishlist.service;

import com.wishlist.exception.DuplicateProductException;
import com.wishlist.model.Wishlist;
import com.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProduct_Success() {
        String customerId = "1";
        String productId = "11";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>());

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(wishlist);
        when(wishlistRepository.save(wishlist)).thenReturn(wishlist);

        Wishlist result = wishlistService.addProduct(customerId, productId);

        assertNotNull(result);
        assertTrue(result.getProductIds().contains(productId));
    }

    @Test
    void addProduct_DuplicateProductException() {
        String customerId = "1";
        String productId = "11";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>(Collections.singletonList(productId)));

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(wishlist);

        // Simulate the scenario where productId already exists in wishlist
        assertThrows(DuplicateProductException.class, () -> {
            wishlistService.addProduct(customerId, productId);
        });
    }

    @Test
    void removeProduct() {
        String customerId = "1";
        String productId = "11";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>(Collections.singletonList(productId)));

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(wishlist);
        when(wishlistRepository.save(wishlist)).thenReturn(wishlist);

        Wishlist result = wishlistService.removeProduct(customerId, productId);

        assertNotNull(result);
        assertFalse(result.getProductIds().contains(productId));
    }

    @Test
    void getWishlist() {
        String customerId = "1";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(wishlist);

        Wishlist result = wishlistService.getWishlist(customerId);

        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
    }

    @Test
    void isProductInWishlist() {
        String customerId = "1";
        String productId = "11";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>(Collections.singletonList(productId)));

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(wishlist);

        boolean result = wishlistService.isProductInWishlist(customerId, productId);

        assertTrue(result);
    }
}
