package siru.fileservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.imgscalr.Scalr;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import siru.fileservice.configuration.properties.FileResourceProp;
import siru.fileservice.domain.file.FileStatus;
import siru.fileservice.domain.file.FileType;
import siru.fileservice.domain.file.ImageFile;
import siru.fileservice.domain.user.AuthUserDetail;
import siru.fileservice.dto.FindImageFileDto;
import siru.fileservice.dto.UploadImageDto;
import siru.fileservice.exception.FileNotFoundException;
import siru.fileservice.exception.FileNotSupportedException;
import siru.fileservice.exception.IllegalRequestException;

import siru.fileservice.repository.ImageFileRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
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
    private final ModelMapper modelMapper;
    private final Tika tika;
    private final FileResourceProp fileResourceProp;

    private static final int IMAGE_THUMBNAIL_WIDTH = 380;
    private static final int IMAGE_THUMBNAIL_HEIGHT = 380;

    /**
     * 파일 url 조회
     * @param fileId
     * @return
     */
    public FindImageFileDto findImageFile(long fileId) {
        ImageFile findFile = imageFileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("파일을 찾을 수 없습니다."));

        FindImageFileDto resultImageFileInfo = modelMapper.map(findFile, FindImageFileDto.class);

        return resultImageFileInfo;
    }

    /**
     * 파일 업로드
     * @param uploadImageInfo
     * @return fileId;
     */
    @Transactional
    public long uploadImageFile(UploadImageDto uploadImageInfo, AuthUserDetail authUserDetail) throws IOException {
        final MultipartFile uploadFile = uploadImageInfo.getUploadFile();
        FileType fileType = uploadImageInfo.getFileType();
        long fileSize = uploadFile.getSize();
        String contentType = uploadFile.getContentType();
        String fileUrl = "";
        String fileCropUrl = "";
        String path = "";
        String fileExtension = "";
        String filePrefix = "";
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        ZonedDateTime current = ZonedDateTime.now();

        if (FileType.PRODUCT_IMAGE == fileType) {
            filePrefix = fileResourceProp.getProductImageUrl() + current.format(dateTimeFormat);
            path = fileResourceProp.getProductImageSavePath() + current.format(dateTimeFormat);
        } else if (FileType.PROFILE_IMAGE == fileType) {
            filePrefix = fileResourceProp.getProfileImageUrl() + current.format(dateTimeFormat);
            path = fileResourceProp.getProfileImageSavePath() + current.format(dateTimeFormat);
        } else {
            throw new IllegalArgumentException("Unknown file type");
        }

        // 이미지 파일 저장할 디렉토리 생성 및 임시파일 생성
        makeDirectory(path);
        File tempFile = File.createTempFile("temp_", UUID.randomUUID().toString() , new File(path));

        /**
         * 파일저장
         */
        try (InputStream uploadFileInputStream = uploadFile.getInputStream()) {
            // input stream 임시파일로 복사
            StreamUtils.copy(uploadFileInputStream, new FileOutputStream(tempFile));

            // media type 추출 및 content type과 비교
            String detectMediaType = tika.detect(tempFile);
            if (StringUtils.isBlank(contentType)) {
                contentType = detectMediaType;
            } else if(!detectMediaType.equals(contentType)) {
                throw new IllegalRequestException("요청한 content type과 실제 파일의 media type이 다릅니다.");
            }

            // 이미지 파일 검증 및 파일 확장자 추출
            if (validateImageType(contentType)) {
                fileExtension = parseImageFileExtension(contentType);
            } else {
                throw new FileNotSupportedException("jpeg, png 이미지만 지원합니다.");
            }

            // 이미지 크기검증
            if (!validateImageSize(tempFile)) {
                throw new IllegalRequestException("이미지 크기는 380x380 이상이어야 합니다.");
            }

            // 이미지 crop 및 저장
            String newFileName = UUID.randomUUID().toString() + fileExtension;
            fileCropUrl = filePrefix + "/" + "thumbnail_" + newFileName;
            makeThumbnail(path + "/" + "thumbnail_" + newFileName, tempFile);

            // 이미지 파일 저장
            fileUrl = filePrefix + "/" + newFileName;
            saveFile(path + "/" + newFileName, tempFile);

        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }

        /**
         * 파일 db 등록
         */
        ImageFile imageFile = ImageFile.builder()
                .fileUrl(fileUrl)
                .fileCropUrl(fileCropUrl)
                .userId(authUserDetail.getUserId())
                .mimeType(contentType)
                .extension(fileExtension)
                .size(fileSize)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .fileType(fileType)
                .status(FileStatus.NORMAL)
                .build();

        imageFileRepository.save(imageFile);
        return imageFile.getId();
    }

    /**
     * 이미지 정방형 crop 및 저장
     * @param path
     * @param imageFile
     */
    private void makeThumbnail(String path, File imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int newWidth = imageWidth;
        int newHeight = (imageWidth * IMAGE_THUMBNAIL_HEIGHT) / IMAGE_THUMBNAIL_WIDTH;

        if (newHeight > imageHeight) {
            newWidth = (imageHeight * IMAGE_THUMBNAIL_WIDTH) / IMAGE_THUMBNAIL_HEIGHT;
            newHeight = imageHeight;
        }

        // 이미지 crop
        BufferedImage cropImage = Scalr.crop(image, (imageWidth - newWidth) / 2, (imageHeight - newHeight) / 2, newWidth, newHeight);

        // 이미지 resizing
        BufferedImage destImage = Scalr.resize(cropImage, IMAGE_THUMBNAIL_WIDTH, IMAGE_THUMBNAIL_HEIGHT);

        // 썸네일 저장
        ImageIO.write(destImage, "JPEG", new File(path));
    }

    /**
     * 이미지 사이즈 검증
     * @param imageFile
     */
    private boolean validateImageSize(File imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        return image.getWidth() >= IMAGE_THUMBNAIL_WIDTH && image.getHeight() >= IMAGE_THUMBNAIL_HEIGHT;
    }

    /**
     * contentType string에서 이미지 확장자 추출 메소드
     * @param contentType
     * @return imageExtension
     */
    private String parseImageFileExtension(String contentType) {
        if(!contentType.startsWith("image/")) {
            throw new IllegalArgumentException(String.format("not image content type %s", contentType));
        }
        return "." + contentType.replace("image/","");
    }

    /**
     * 이미지 타입 검증 (jpeg, png 지원여부 검증)
     * @param contentType
     */
    private boolean validateImageType(String contentType) {
        return contentType.contains("image/jpeg") || contentType.contains("image/png");
    }

    /**
     * 파일을 저장하는 메소드
     * @param path
     * @param src
     * @return
     */
    private void saveFile(String path, File src) throws IOException{
        File targetFile = new File(path);
        FileCopyUtils.copy(src, targetFile);
    }

    /**
     * 디렉토리 생성 메소드
     * @param path
     */
    private void makeDirectory(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IOException("fail to directory to save image file");
            }
        }
    }

}
