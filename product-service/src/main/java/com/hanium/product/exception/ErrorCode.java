package com.hanium.product.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Method not allowed"),
    NOT_FOUND(404, "C003", " Not Found"),
    INVALID_TYPE_VALUE(400, "C004", " Invalid Type Value"),
    UN_AUTHENTICATION(401, "C005", "Not Authentication"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),
    INTERNAL_SERVER_ERROR(500, "C007", "Server error"),

    // Product
    PRODUCT_NOT_FOUND(404, "P001", "Product not found"),
    PRODUCT_ALREADY_EXIST(409, "P002", "Product already exist"),
    INVALID_CATEGORY_ID(400, "P003", "Invalid category id"),
    PRODUCT_ALREADY_DELETE(409, "P004", "Already product delete")
    ;

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}