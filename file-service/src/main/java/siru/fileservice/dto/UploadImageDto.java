package siru.fileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import siru.fileservice.domain.file.FileType;

import java.util.List;

/**
 * @author siru
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadImageDto {
    private FileType fileType;
    private MultipartFile uploadFile;
}