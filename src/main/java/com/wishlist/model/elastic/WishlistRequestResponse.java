package com.wishlist.model.elastic;

import com.wishlist.dto.AddProductRequest;
import com.wishlist.model.Wishlist;

public class WishlistRequestResponse {

    private AddProductRequest requestPayload;
    private Wishlist responsePayload;

    public WishlistRequestResponse(AddProductRequest requestPayload, Wishlist responsePayload) {
        this.requestPayload = requestPayload;
        this.responsePayload = responsePayload;
    }

    public AddProductRequest getRequestPayload() {
        return requestPayload;
    }

    public void setRequestPayload(AddProductRequest requestPayload) {
        this.requestPayload = requestPayload;
    }

    public Wishlist getResponsePayload() {
        return responsePayload;
    }

    public void setResponsePayload(Wishlist responsePayload) {
        this.responsePayload = responsePayload;
    }
}
