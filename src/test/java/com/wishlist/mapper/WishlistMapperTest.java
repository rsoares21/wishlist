package com.wishlist.mapper;

import com.wishlist.model.Wishlist;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class WishlistMapperTest {

    private final WishlistMapper wishlistMapper = Mappers.getMapper(WishlistMapper.class);

    @Test
    void toWishlistTest() {
        String customerId = "1";
        String productId = "11";

        Wishlist wishlist = wishlistMapper.toWishlist(customerId, productId);

        assertNotNull(wishlist);
        assertEquals(customerId, wishlist.getCustomerId());
        assertEquals(1, wishlist.getProductIds().size());
        assertEquals(productId, wishlist.getProductIds().get(0));
    }
}
