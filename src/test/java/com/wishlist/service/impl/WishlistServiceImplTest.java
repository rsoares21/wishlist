package com.wishlist.service.impl;

import com.wishlist.dto.AddProductRequest;
import com.wishlist.exception.DuplicateProductException;
import com.wishlist.exception.FulllWishlistException;
import com.wishlist.exception.NotTheAccountOwnerException;
import com.wishlist.model.Wishlist;
import com.wishlist.repository.WishlistRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceImplTest {

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ElasticsearchServiceImpl elasticsearchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProduct_ShouldAddProduct_WhenValidRequest() throws IOException {
        String customerId = "1";
        String productId = "11";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>());

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(wishlist);
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        Wishlist result = wishlistService.addProduct(customerId, productId);

        assertNotNull(result);
        assertEquals(1, result.getProductIds().size());
        verify(wishlistRepository).save(wishlist);
        verify(elasticsearchService).indexWishlistAddProduct(any(AddProductRequest.class), eq(wishlist));
    }

    @Test
    void addProduct_ShouldThrowNotTheAccountOwnerException_WhenInvalidCustomerId() {
        String customerId = "2"; // Not a valid customerId for the test
        String productId = "11";

        assertThrows(NotTheAccountOwnerException.class, () -> wishlistService.addProduct(customerId, productId));
    }

    @Test
    void addProduct_ShouldThrowFulllWishlistException_WhenWishlistIsFull() {
        String customerId = "1";
        String productId = "21";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20")));

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(wishlist);

        assertThrows(FulllWishlistException.class, () -> wishlistService.addProduct(customerId, productId));
    }

    @Test
    void addProduct_ShouldThrowDuplicateProductException_WhenProductAlreadyInWishlist() {
        String customerId = "1";
        String productId = "11";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>(Arrays.asList("11")));

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(wishlist);

        assertThrows(DuplicateProductException.class, () -> wishlistService.addProduct(customerId, productId));
    }

    @Test
    void removeProduct_ShouldRemoveProduct_WhenValidRequest() {
        String customerId = "1";
        String productId = "11";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>(Arrays.asList(productId)));

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(wishlist);
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        Wishlist result = wishlistService.removeProduct(customerId, productId);

        assertNotNull(result);
        assertTrue(result.getProductIds().isEmpty());
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void getWishlist_ShouldReturnWishlist_WhenValidCustomerId() {
        String customerId = "1";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>(Arrays.asList("11", "12")));

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(wishlist);

        Wishlist result = wishlistService.getWishlist(customerId);

        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals(2, result.getProductIds().size());
    }

    @Test
    void isProductInWishlist_ShouldReturnTrue_WhenProductIsInWishlist() {
        String customerId = "1";
        String productId = "11";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>(Arrays.asList(productId)));

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(wishlist);

        boolean result = wishlistService.isProductInWishlist(customerId, productId);

        assertTrue(result);
    }

    @Test
    void isProductInWishlist_ShouldReturnFalse_WhenProductIsNotInWishlist() {
        String customerId = "1";
        String productId = "11";
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId(customerId);
        wishlist.setProductIds(new ArrayList<>());

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(wishlist);

        boolean result = wishlistService.isProductInWishlist(customerId, productId);

        assertFalse(result);
    }

    @Test
    void addProductRequest_ShouldAddProduct_WhenValidRequest() throws IOException {
        AddProductRequest request = new AddProductRequest("1", "11");
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomerId("1");
        wishlist.setProductIds(new ArrayList<>());

        when(wishlistRepository.findByCustomerId("1")).thenReturn(wishlist);
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        Wishlist result = wishlistService.addProductRequest(request);

        assertNotNull(result);
        assertEquals(1, result.getProductIds().size());
        verify(wishlistRepository).save(wishlist);
        verify(elasticsearchService).indexWishlistAddProduct(request, wishlist);
    }

    @Test
    void addProduct_throwsNotTheAccountOwnerException() {
        String customerId = "2";
        String productId = "11";

        assertThrows(NotTheAccountOwnerException.class, () -> {
            wishlistService.addProduct(customerId, productId);
        });
    }

    @Test
    public void testRemoveProduct_WishlistNotFound() {
        String customerId = "1";
        String productId = "P001";

        // Mocking behavior of repository
        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(null);

        // Test removeProduct method with wishlist not found
        Wishlist updatedWishlist = wishlistService.removeProduct(customerId, productId);

        // Verify repository method was called once for findByCustomerId
        verify(wishlistRepository, times(1)).findByCustomerId(customerId);

        // Verify repository method was not called for save
        verify(wishlistRepository, never()).save(any());

        // Assert that null is returned when wishlist is not found
        assertNull(updatedWishlist);
    }

    @Test
    public void testRemoveProduct_ProductNotInWishlist() {

        String customerId = "1";
        String productId = "55";

        // Mockando o comportamento do repositório para retornar null, simulando produto
        // não encontrado
        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(null);

        // Chama método de serviço
        Wishlist updatedWishlist = wishlistService.removeProduct(customerId, productId);

        // Verificando que findByCustomerId foi chamado uma vez
        verify(wishlistRepository, times(1)).findByCustomerId(customerId);

        // Verificando que save nunca foi chamado porque a lista de desejos não foi
        // atualizada
        verify(wishlistRepository, never()).save(any());

        // Assegurando que updatedWishlist seja null pois o produto não foi encontrado
        assertNull(updatedWishlist);
    }

}
