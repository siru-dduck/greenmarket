package siru.fileservice.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import siru.fileservice.domain.file.FileType;
import siru.fileservice.dto.FindImageFileDto;
import siru.fileservice.dto.UploadImageDto;
import siru.fileservice.service.FileService;

import java.io.IOException;
import java.net.URI;

/**
 * 파일 업로드, 다운로드 컨트롤러
 * @author siru
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/file-service")
public class FileController {

    private final FileService fileService;

    private static final String GET_ORIGIN_IMAGE_PATH = "/image/{fileId}/origin";

    @ApiOperation(value = "이미지 파일 리다이렉트", notes = "파일 원본 이미지 조회후 리소스 경로로 리다렉트")
    @ApiResponses({
            @ApiResponse(code = 303, message = "see other"),
            @ApiResponse(code = 404, message = "file not found")
    })
    @GetMapping(GET_ORIGIN_IMAGE_PATH)
    public ResponseEntity<Void> getOriginImage(@PathVariable long fileId) {
        FindImageFileDto imageFileInfo = fileService.findImageFile(fileId);
        String fileUrl = imageFileInfo.getFileUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(fileUrl));
        return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).build();
    }

    @ApiOperation(value = "썸네일 이미지 파일 리다이렉트", notes = "파일 썸네일 이미지 조회후 리소스 경로로 리다렉트")
    @ApiResponses({
            @ApiResponse(code = 303, message = "see other"),
            @ApiResponse(code = 404, message = "file not found")
    })
    @GetMapping("/image/{fileId}/thumbnail")
    public ResponseEntity<Void> getThumbnailImage(@PathVariable long fileId) {
        FindImageFileDto imageFileInfo = fileService.findImageFile(fileId);
        String fileUrl = imageFileInfo.getFileCropUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(fileUrl));
        return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).build();
    }

    @ApiOperation(value = "이미지 파일 업로드", notes = "상품 파일 이미지 업로드")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @PostMapping(value = "/image/{fileType}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UploadResponse> uploadProductImage(@PathVariable FileType fileType
            , @RequestParam MultipartFile file) throws IOException {
        log.info("Upload {} file: {} {}", fileType, file.getContentType(), file.getSize());

        // 파일 업로드
        UploadImageDto uploadImageInfo = UploadImageDto.builder()
                .fileType(fileType)
                .uploadFile(file)
                .build();
        long fileId = fileService.uploadImageFile(uploadImageInfo);

        // 응답
        UploadResponse response = UploadResponse.builder()
                .fileId(fileId)
                .result("success")
                .build();

        return ResponseEntity.ok(response);
    }

}
