package com.hanium.product.exception;

public class InvalidCategoryIdException extends BusinessException {
    public InvalidCategoryIdException(String message) {
        super(message, ErrorCode.INVALID_CATEGORY_ID);
    }
}
