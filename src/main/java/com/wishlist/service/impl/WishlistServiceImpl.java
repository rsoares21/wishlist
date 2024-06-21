package com.wishlist.service.impl;

import com.wishlist.dto.AddProductRequest;
import com.wishlist.exception.DuplicateProductException;
import com.wishlist.exception.FulllWishlistException;
import com.wishlist.exception.NotTheAccountOwnerException;
import com.wishlist.mapper.WishlistMapper;
import com.wishlist.model.Wishlist;
import com.wishlist.repository.WishlistRepository;
import com.wishlist.service.WishlistService;
import com.wishlist.utils.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
@EnableCaching
public class WishlistServiceImpl implements WishlistService {

    private static final Logger logger = LoggerFactory.getLogger(WishlistServiceImpl.class);

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ElasticsearchServiceImpl elasticsearchService;

    @Autowired
    private WishlistMapper wishlistMapper;

    @Cacheable(value = "wishlistCache", key = "#customerId")
    @Override
    public Wishlist getWishlist(String customerId) {
        logger.info("Fetching wishlist from database for customer ID: {}", customerId);
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        return wishlist != null ? wishlist : new Wishlist(); // Return an empty Wishlist if null
    }

    @CacheEvict(value = "wishlistCache", key = "#customerId")
    @Override
    public Wishlist addProduct(String customerId, String productId) {
        if (customerId != null && !customerId.equals("1")) {
            throw new NotTheAccountOwnerException("Usuário inválido.");
        }
    
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
    
        if (wishlist == null) {
            wishlist = wishlistMapper.toWishlist(customerId, productId);
        }
    
        if (wishlist.getProductIds().contains(productId)) {
            throw new DuplicateProductException("Produto já existe na lista de desejos.");
        }
    
        if (wishlist.getProductIds().size() < Constants.MAX_PRODUCTS_IN_WHISHLIST) {
            wishlist.getProductIds().add(productId);
            Wishlist savedWishlist = wishlistRepository.save(wishlist); // Salva a wishlist no repositório após adicionar o produto
    
            logger.info("Wishlist saved to database: {}", savedWishlist);
    
            try {
                elasticsearchService.indexWishlistAddProduct(new AddProductRequest(customerId, productId), savedWishlist);
            } catch (IOException e) {
                logger.error("Error indexing wishlist to Elasticsearch: {}", e.getMessage());
            }
    
            return savedWishlist; // Retorna a wishlist salva após a operação
        } else {
            throw new FulllWishlistException(String.format("Wishlist pode ter no máximo %d produtos.", Constants.MAX_PRODUCTS_IN_WHISHLIST));
        }
    }
    

    @CacheEvict(value = "wishlistCache", key = "#customerId")
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
    public boolean isProductInWishlist(String customerId, String productId) {
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId);
        return wishlist != null && wishlist.getProductIds().contains(productId);
    }

    @CacheEvict(value = "wishlistCache", key = "#request.customerId")
    @Override
    public Wishlist addProductRequest(AddProductRequest request) {
        if (request.getCustomerId() != null && !request.getCustomerId().equals("1")) {
            throw new NotTheAccountOwnerException("Usuário inválido.");
        }

        Wishlist wishlist = wishlistRepository.findByCustomerId(request.getCustomerId());

        if (wishlist == null) {
            wishlist = new Wishlist();
            wishlist.setCustomerId(request.getCustomerId());
            wishlist.setProductIds(new ArrayList<>());
        }

        if (wishlist.getProductIds().contains(request.getProductId())) {
            throw new DuplicateProductException("Produto já existe na lista de desejos.");
        }

        if (wishlist.getProductIds().size() < Constants.MAX_PRODUCTS_IN_WHISHLIST) {
            wishlist.getProductIds().add(request.getProductId());
            Wishlist savedWishlist = wishlistRepository.save(wishlist);

            logger.info("Wishlist saved to database: {}", savedWishlist);

            try {
                elasticsearchService.indexWishlistAddProduct(request, savedWishlist);
            } catch (IOException e) {
                logger.error("Error indexing wishlist to Elasticsearch: {}", e.getMessage());
            }

            return savedWishlist;
        } else {
            throw new FulllWishlistException(String.format("Wishlist pode ter no máximo %d produtos.", Constants.MAX_PRODUCTS_IN_WHISHLIST));
        }
    }
}
