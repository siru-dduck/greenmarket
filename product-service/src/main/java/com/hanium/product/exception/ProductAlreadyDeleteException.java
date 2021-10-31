package com.hanium.product.exception;

public class ProductAlreadyDeleteException extends BusinessException {
    public ProductAlreadyDeleteException(String message) {
        super(message, ErrorCode.PRODUCT_ALREADY_DELETE);
    }
}
