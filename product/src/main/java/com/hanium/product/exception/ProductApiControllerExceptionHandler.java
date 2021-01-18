package com.hanium.product.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class ProductApiControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(ex.getMessage())
                .details(ex.getBindingResult().toString())
                .timestamp(new Date())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(ex.getMessage())
                .details(ex.getBindingResult().toString())
                .timestamp(new Date())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotSavedFileException.class)
    public ResponseEntity<ExceptionResponse> handleNotSavedFileException (NotSavedFileException ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message("Not Saved File")
                .details(ex.getMessage())
                .timestamp(new Date())
                .build();
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AlreadyExistResourceException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyExistResourceException (AlreadyExistResourceException ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message("Conflict Resource")
                .details(ex.getMessage())
                .timestamp(new Date())
                .build();
        return new ResponseEntity(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundResourceException (NotFoundResourceException ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message("Not Found Resource")
                .details(ex.getMessage())
                .timestamp(new Date())
                .build();
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorizationException (AuthorizationException ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message("Not Authorized")
                .details(ex.getMessage())
                .timestamp(new Date())
                .build();
        return new ResponseEntity(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message("UnAuthenticated")
                .details(ex.getMessage())
                .timestamp(new Date())
                .build();
        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .timestamp(new Date())
                .build();
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
