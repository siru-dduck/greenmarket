package siru.fileservice.exception;

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
    ILLEGAL_REQUEST(403, "C008", "Illegal request"),
    MEDIA_TYPE_NOT_SUPPORTED(415, "C009", "Media not supported"),

    // File
    MULTIPART_EXCEPTION(400, "F001", "invalid multipart form"),
    FILE_NOT_FOUND(404, "F002", "file not found"),
    FILE_NOT_SUPPORTED(415, "F003", "file not supported");


    private final String code;
    private final String message;

    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}