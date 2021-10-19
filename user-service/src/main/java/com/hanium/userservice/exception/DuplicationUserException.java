package com.hanium.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicationUserException extends RuntimeException {
    public DuplicationUserException() {
        super();
    }

    public DuplicationUserException(String message) {
        super(message);
    }

    public DuplicationUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicationUserException(Throwable cause) {
        super(cause);
    }

    protected DuplicationUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
