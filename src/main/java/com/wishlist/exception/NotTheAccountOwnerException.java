package com.wishlist.exception;

public class NotTheAccountOwnerException extends RuntimeException {

    public NotTheAccountOwnerException(String message) {
        super(message);
    }
}
