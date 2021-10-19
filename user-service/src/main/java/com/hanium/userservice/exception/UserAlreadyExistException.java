package com.hanium.userservice.exception;

public class UserAlreadyExistException extends BusinessException {
    public UserAlreadyExistException(String message) {
        super(message, ErrorCode.USER_ALREADY_EXIST);
    }
}
