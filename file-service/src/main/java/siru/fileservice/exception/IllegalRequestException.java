package siru.fileservice.exception;

public class IllegalRequestException extends BusinessException {

    public IllegalRequestException(String message) {
        super(message, ErrorCode.ILLEGAL_REQUEST);
    }
}
