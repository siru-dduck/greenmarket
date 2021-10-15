package siru.fileservice.controller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadResponse {

    @ApiModelProperty(name = "파일 id", notes = "업로드한 파일 id")
    private long fileId;

    @ApiModelProperty(name = "업로드 결과", notes = "업로드 결과")
    private String result;

}
