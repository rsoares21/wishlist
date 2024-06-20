package com.wishlist.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wishlist.dto.AddProductRequest;
import com.wishlist.model.Wishlist;
import com.wishlist.model.elastic.WishlistRequestResponse;
import com.wishlist.service.ElasticsearchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchServiceImpl.class);

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    public void indexWishlistAddProduct(AddProductRequest reqWishlist, Wishlist responsePayload) throws IOException {
        
        WishlistRequestResponse requestResponse = new WishlistRequestResponse(reqWishlist, responsePayload);
        
        IndexRequest<WishlistRequestResponse> request = IndexRequest.of(i -> i
                .index("wishlist-add-product")
                .document(requestResponse)
        );

        IndexResponse indexResponse = elasticsearchClient.index(request);
        logger.info("Wishlist request/response indexed to Elasticsearch: {}", indexResponse.result());
    }
}