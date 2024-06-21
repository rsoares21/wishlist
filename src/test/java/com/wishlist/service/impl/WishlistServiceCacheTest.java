package com.wishlist.service.impl;

import com.wishlist.model.Wishlist;
import com.wishlist.repository.WishlistRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WishlistServiceCacheTest {

    @Autowired
    private WishlistServiceImpl wishlistService;

    @MockBean
    private WishlistRepository wishlistRepository;

    @Mock
    private ElasticsearchServiceImpl elasticsearchService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Limpar o cache do Redis antes de cada teste
        RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory).build();
        cacheManager.getCache("wishlistCache").clear();
    }

    @Test
    public void testCacheHit() {
        String customerId = "123";

        // Mock repository to return a Wishlist
        Wishlist mockWishlist = new Wishlist();
        mockWishlist.setCustomerId(customerId);
        mockWishlist.setProductIds(new ArrayList<>());

        // Stubbing the repository method
        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(mockWishlist);

        // Call the service method under test
        Wishlist wishlist1 = wishlistService.getWishlist(customerId);

        // Call the service method again to hit the cache
        Wishlist wishlist2 = wishlistService.getWishlist(customerId);

        // Verify that repository method was called exactly once
        verify(wishlistRepository, times(1)).findByCustomerId(customerId);

        // Assert that the content of the returned objects is the same
        assertEquals(wishlist1, wishlist2);
    }
}
