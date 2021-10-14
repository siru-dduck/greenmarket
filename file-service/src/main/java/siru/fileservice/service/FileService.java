package siru.fileservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import siru.fileservice.configuration.properties.FileResourceProp;
import siru.fileservice.domain.file.FileStatus;
import siru.fileservice.domain.file.FileType;
import siru.fileservice.domain.file.ImageFile;
import siru.fileservice.dto.UploadImageDto;
import siru.fileservice.exception.FileSaveException;
import siru.fileservice.exception.NotSupportedException;
import siru.fileservice.repository.ImageFileRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 파일 처리 서비스
 * @author siru
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final ImageFileRepository imageFileRepository;
    private final Tika tika;
    private final FileResourceProp fileResourceProp;

    /**
     * 파일 업로드
     * @param uploadImageInfo
     * @return fileId;
     */
    @Transactional
    public long uploadImageFile(UploadImageDto uploadImageInfo) throws IOException {
        final MultipartFile uploadFile = uploadImageInfo.getUploadFile();
        String contentType = uploadFile.getContentType();
        long fileSize = uploadFile.getSize();
        FileType fileType = uploadImageInfo.getFileType();
        String fileUrl = "";
        String path = "";
        String originalFileExtension = "";

        /**
         * 파일저장
         */
        try (InputStream uploadFileInputStream = uploadFile.getInputStream()) {
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
            ZonedDateTime current = ZonedDateTime.now();

            if (FileType.PRODUCT_IMAGE == fileType) {
                fileUrl = fileResourceProp.getProductImageUrl() + current.format(dateTimeFormat);
                path = fileResourceProp.getProductImageSavePath() + current.format(dateTimeFormat);
            } else if (FileType.PROFILE_IMAGE == fileType) {
                fileUrl = fileResourceProp.getProductImageUrl() + current.format(dateTimeFormat);
                path = fileResourceProp.getProductImageSavePath() + current.format(dateTimeFormat);
            } else {
                throw new IllegalArgumentException("Unknown file type");
            }

            File file = new File(path);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    throw new FileSaveException("fail to derectory to save image file");
                }
            }

            String newFileName;

            // media type 추출
            if (StringUtils.isBlank(contentType)) {
                contentType = tika.detect(uploadFileInputStream);
            }

            if (contentType.contains("image/jpeg")) {
                originalFileExtension = ".jpeg";
            } else if (contentType.contains("image/png")) {
                originalFileExtension = ".png";
            } else {
                throw new NotSupportedException("jpeg, png 파일만 지원합니다.");
            }

            newFileName = UUID.randomUUID().toString() + originalFileExtension;
            File targetFile = new File(path + "/" + newFileName);
            try {
                org.apache.commons.io.FileUtils.copyInputStreamToFile(uploadFileInputStream, targetFile);
            } catch (IOException e) {
                throw new FileSaveException("파일을 저장할 수 없습니다.");
            }

            /**
             *  TODO 이미지 정방형 CROP 로직추가
             */
        }

        /**
         * 파일 db 등록
         */
        ImageFile imageFile = ImageFile.builder()
                .fileUrl(fileUrl)
                .fileCropUrl(fileUrl) // TODO 수정
                .userId(-999L) // TODO 시큐리티 적용후 수정
                .mimeType(contentType)
                .extension(originalFileExtension)
                .size(fileSize)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .fileType(fileType)
                .status(FileStatus.NORMAL)
                .build();

        imageFileRepository.save(imageFile);
        return imageFile.getId();
    }

}
