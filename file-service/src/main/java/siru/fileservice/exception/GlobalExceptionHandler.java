package siru.fileservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * @author siru
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleMissingServletRequestPart", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleBindException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException e, HttpHeaders headers, HttpStatus status, WebRequest request)  {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MEDIA_TYPE_NOT_SUPPORTED);
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleMultipartException(MultipartException ex) {
        log.error("handleMultipartException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(UserAuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleUnAuthenticationException(UserAuthenticationException e) {
        log.error("handleAccessDeniedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.UN_AUTHENTICATION);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.UN_AUTHENTICATION.getStatus()));
    }

    /**
     * 인가 관련 예외 처리시 발생
     */
    @ExceptionHandler(UserAuthorizationException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(UserAuthorizationException e) {
        log.error("handleAccessDeniedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.HANDLE_ACCESS_DENIED.getStatus()));
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.error("handleBusinessException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleEntityNotFoundException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}