package com.wishlist.exception;

public class WishlistNotFoundException extends RuntimeException {

    public WishlistNotFoundException() {
        super();
    }

    public WishlistNotFoundException(String message) {
        super(message);
    }

    public WishlistNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
