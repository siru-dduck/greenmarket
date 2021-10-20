package com.hanium.userservice.exception;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message) {
        super(message, ErrorCode.USER_NOT_FOUND);
    }
}
