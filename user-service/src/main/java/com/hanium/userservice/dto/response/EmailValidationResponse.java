package com.hanium.userservice.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmailValidationResponse {

    @ApiModelProperty(name = "이매일 중복 여부 결과", notes = "exist(이메일 존재함), notExist(이메일이 존재하지 않음)")
    private Result result;

    @ApiModelProperty(name = "", notes = "")
    private String email;

    public enum Result {
        exist, notExist
    }
}
