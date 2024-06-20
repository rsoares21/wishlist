package com.wishlist.service.impl;

import com.wishlist.dto.AddProductRequest;
import com.wishlist.exception.DuplicateProductException;
import com.wishlist.exception.FulllWishlistException;
import com.wishlist.exception.NotTheAccountOwnerException;
import com.wishlist.model.Wishlist;
import com.wishlist.repository.WishlistRepository;
import com.wishlist.service.WishlistService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class WishlistServiceImpl implements WishlistService {

    private static final Logger logger = LoggerFactory.getLogger(WishlistServiceImpl.class);

    @Value("${wishlist.max-products}")
    int maxProducts;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ElasticsearchServiceImpl elasticsearchService;

    @Override
    public Wishlist addProduct(String customerId, String productId) {
        
        logger.info("customerId:" + customerId);

        // Apenas para efeito de testes, se o customerId for diferente de 1, lançar uma exceção.
        if (customerId != null && !customerId.equals("1")) {
            throw new NotTheAccountOwnerException("Usuário inválido.");
        }
        
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
            Wishlist savedWishlist = wishlistRepository.save(wishlist);

            logger.info("Wishlist saved to database: {}", savedWishlist);

            try {
                elasticsearchService.indexWishlistAddProduct(new AddProductRequest(customerId,productId), savedWishlist);
            } catch (IOException e) {
                logger.error("Error indexing wishlist to Elasticsearch: {}", e.getMessage());
            }
            
            return savedWishlist;
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
            Wishlist savedWishlist = wishlistRepository.save(wishlist);

            logger.info("Wishlist saved to database: {}", savedWishlist);

            try {
                elasticsearchService.indexWishlistAddProduct(request, savedWishlist);
            } catch (IOException e) {
                logger.error("Error indexing wishlist to Elasticsearch: {}", e.getMessage());
            }

            return savedWishlist;

        } else {
            throw new FulllWishlistException(String.format("Wishlist pode ter no máximo %d produtos.", maxProducts));
        }
    }
}
