package siru.fileservice.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import siru.fileservice.domain.file.FileType;
import siru.fileservice.dto.UploadImageDto;
import siru.fileservice.service.FileService;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.net.URI;

/**
 * 파일 업로드, 다운로드 컨트롤러
 * @author siru
 */
@Slf4j
@RequiredArgsConstructor
@RestController
//@RequestMapping("/api/file-service")
public class FileController {

    private final FileService fileService;

    private static final String GET_UPLOAD_IMAGE_PATH = "/upload/{fileId}/image";

    @ApiOperation(value = "이미지 파일 리다이렉트", notes = "상품 파일 이미지 조회후 리소스 경로로 리다렉트")
    @ApiResponses({
            @ApiResponse(code = 303, message = "see other"),
            @ApiResponse(code = 404, message = "file not found")
    })
    @GetMapping(GET_UPLOAD_IMAGE_PATH)
    public ResponseEntity<Void> getProductImage(@PathVariable long fileId) {
        HttpHeaders headers = new HttpHeaders();
        String fileUrl = fileService.findFileUrl(fileId);
        headers.setLocation(URI.create(fileUrl));
        return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).build();
    }

    /**
     * TODO validation
     */
    @ApiOperation(value = "이미지 파일 업로드", notes = "상품 파일 이미지 업로드")
    @ApiResponses({
            @ApiResponse(code = 201, message = "created")
    })
    @PostMapping("/upload/{fileType}/image")
    public ResponseEntity<Void> uploadProductImage(@PathVariable FileType fileType
            , @NotEmpty(message = "파일은 필수로 첨부해야합니다.") MultipartFile file) throws IOException {
        log.info("Upload {} file: {} {}", fileType, file.getContentType(), file.getSize());

        // 파일 업로드
        UploadImageDto uploadImageInfo = UploadImageDto.builder()
                .fileType(fileType)
                .uploadFile(file)
                .build();
        long resultFileId = fileService.uploadImageFile(uploadImageInfo);

        // 응답
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(GET_UPLOAD_IMAGE_PATH.replace("{fileId}", String.valueOf(resultFileId))));

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
    }

    // TODO Exception Handling
}
