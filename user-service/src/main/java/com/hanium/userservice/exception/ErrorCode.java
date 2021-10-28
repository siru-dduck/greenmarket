package com.hanium.userservice.exception;

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

    // Member
    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),
    USER_ALREADY_EXIST(409, "M002", "User already exist"),
    USER_NOT_FOUND(404, "M003", "User not found")
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