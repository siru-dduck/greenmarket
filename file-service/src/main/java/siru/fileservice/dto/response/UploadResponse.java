package siru.fileservice.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadResponse {

    @ApiModelProperty(name = "파일 id", notes = "업로드한 파일 id")
    private long fileId;

    @ApiModelProperty(name = "업로드 결과", notes = "업로드 결과")
    private Result result;

     public enum Result {
        SUCCESS, FAIL
    }

}
