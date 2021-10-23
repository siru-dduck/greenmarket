package siru.fileservice.exception;

/**
 * @author siru
 */
public class FileNotFoundException extends BusinessException {

    public FileNotFoundException(String message) {
        super(message, ErrorCode.FILE_NOT_FOUND);
    }
}