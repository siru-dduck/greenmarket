package siru.fileservice.dto;

import lombok.*;
import siru.fileservice.domain.file.FileStatus;
import siru.fileservice.domain.file.FileType;

import java.time.LocalDateTime;

/**
 * @author siru
 */
@Data
public class FindImageFileDto {
    private Long id;

    private String fileCropUrl;

    private String fileUrl;

    private Long userId;

    private String mimeType;

    private String extension;

    private Long size;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private FileType fileType;

    private FileStatus status;
}
