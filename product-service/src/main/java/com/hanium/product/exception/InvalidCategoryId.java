package com.hanium.product.exception;

public class InvalidCategoryId extends BusinessException {
    public InvalidCategoryId(String message) {
        super(message, ErrorCode.INVALID_CATEGORY_ID);
    }
}
