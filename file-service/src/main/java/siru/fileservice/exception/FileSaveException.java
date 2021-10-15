package siru.fileservice.exception;

/**
 * @author siru
 */
public class FileSaveException extends RuntimeException{
    public FileSaveException(String message) {
        super(message);
    }

    public FileSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSaveException(Throwable cause) {
        super(cause);
    }

    protected FileSaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
