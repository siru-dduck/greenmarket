package com.hanium.product.exception;

public class AlreadyExistResourceException extends RuntimeException {
    public AlreadyExistResourceException(String message) {
        super(message);
    }
}
