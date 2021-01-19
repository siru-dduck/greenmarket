package com.hanium.product.exception;

public class NotFoundResourceException extends RuntimeException{
    public NotFoundResourceException(String message) {
        super(message);
    }
}
