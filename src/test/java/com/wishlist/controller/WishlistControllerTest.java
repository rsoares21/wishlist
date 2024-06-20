package com.wishlist.controller;

import com.wishlist.model.Wishlist;
import com.wishlist.service.impl.WishlistService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WishlistController.class)
public class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProduct() throws Exception {
        String customerId = "1";
        String productId = "11";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>(Arrays.asList(productId)));

        when(wishlistService.addProduct(customerId, productId)).thenReturn(wishlist);

        mockMvc.perform(post("/wishlist/{customerId}/add/{productId}", customerId, productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.productIds[0]").value(productId));
    }

    @Test
    void removeProduct() throws Exception {
        String customerId = "1";
        String productId = "11";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>());

        when(wishlistService.removeProduct(customerId, productId)).thenReturn(wishlist);

        mockMvc.perform(delete("/wishlist/{customerId}/remove/{productId}", customerId, productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.productIds").isEmpty());
    }

    @Test
    void getWishlist() throws Exception {
        String customerId = "1";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>(Arrays.asList("11", "12")));

        when(wishlistService.getWishlist(customerId)).thenReturn(wishlist);

        mockMvc.perform(get("/wishlist/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.productIds[0]").value("11"))
                .andExpect(jsonPath("$.productIds[1]").value("12"));
    }

    @Test
    void isProductInWishlist() throws Exception {
        String customerId = "1";
        String productId = "11";

        when(wishlistService.isProductInWishlist(customerId, productId)).thenReturn(true);

        mockMvc.perform(get("/wishlist/{customerId}/contains/{productId}", customerId, productId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
