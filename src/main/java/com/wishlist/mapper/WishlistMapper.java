package com.wishlist.mapper;

import com.wishlist.model.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WishlistMapper {

    @Mapping(source = "customerId", target = "customerId")
    @Mapping(target = "productIds", expression = "java(new java.util.ArrayList<>(java.util.Arrays.asList(productId)))")
    Wishlist toWishlist(String customerId, String productId);
}
