package siru.fileservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(generateErrorResponse(HttpStatus.BAD_REQUEST.value(), ex));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleMultipartException(MultipartException ex) {
        return ResponseEntity.badRequest().body(generateErrorResponse(HttpStatus.BAD_REQUEST.value(), ex));
    }

    @ExceptionHandler(NotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleNotSupportedException(NotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(generateErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), ex));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(generateErrorResponse(HttpStatus.NOT_FOUND.value(), ex));
    }

    @ExceptionHandler(IllegalRequestException.class)
    public ResponseEntity<ErrorResponse> handleIllegalRequestException(IllegalRequestException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(generateErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), ex));
    }

    @ExceptionHandler(FileSaveException.class)
    public ResponseEntity<ErrorResponse> handleFileSaveException(FileSaveException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(generateErrorResponse(HttpStatus.BAD_REQUEST.value(), ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("", ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(generateErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), ex));
    }

    private ErrorResponse generateErrorResponse(int status, Exception ex) {
        return ErrorResponse.builder()
                .status(status)
                .message(ex.getMessage())
                .build();
    }

}
