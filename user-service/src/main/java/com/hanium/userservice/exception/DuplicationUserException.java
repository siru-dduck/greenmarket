package com.hanium.userservice.exception;

/**
 * @author siru
 */
public class DuplicationUserException extends BusinessException {
    public DuplicationUserException(String message) {
        super(message, ErrorCode.EMAIL_DUPLICATION);
    }
}
