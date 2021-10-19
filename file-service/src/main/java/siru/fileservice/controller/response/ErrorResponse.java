package siru.fileservice.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ErrorResponse {

    @ApiModelProperty(name = "응답 메세지", notes = "응답 메세지")
    private String message;

    @ApiModelProperty(name = "http status", notes = "http status")
    private int status;

    @ApiModelProperty(name = "에러 목록", notes = "에러 목록")
    private List<FieldError> errors;

    @ApiModelProperty(name = "응답 에러코드", notes = "응답 에러코드")
    private String code;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {

        @ApiModelProperty(name = "에러 필드명", notes = "에러 필드명")
        private String field;

        @ApiModelProperty(name = "에러 필드 값", notes = "에러 필드 값")
        private String value;

        @ApiModelProperty(name = "에러 원인", notes = "에러 원인")
        private String reason;
    }
}