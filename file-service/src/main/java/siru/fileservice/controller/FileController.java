package siru.fileservice.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/api/file-service")
public class FileController {

    @ApiOperation(value = "상품 파일 이미지 조회", notes = "상품 파일 이미지 조회후 리소스 경로로 리다렉트")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 404, message = "page not found!")
    })
    @GetMapping("/upload/products/{fileId}/image")
    public ResponseEntity<Void> getProductImage(@PathVariable long fileId) {
        log.info("Get Product Image {}", fileId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
