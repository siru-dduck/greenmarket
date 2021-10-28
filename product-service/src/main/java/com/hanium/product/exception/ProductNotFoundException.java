package com.hanium.product.exception;

public class ProductNotFoundException extends BusinessException {
    public ProductNotFoundException(String message) {
        super(message, ErrorCode.PRODUCT_NOT_FOUND);
    }
}
