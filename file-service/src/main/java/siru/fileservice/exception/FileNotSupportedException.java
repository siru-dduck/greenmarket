package siru.fileservice.exception;

/**
 * @author siru
 */
public class FileNotSupportedException extends BusinessException {

    public FileNotSupportedException(String message) {
        super(message, ErrorCode.FILE_NOT_SUPPORTED);
    }
}
