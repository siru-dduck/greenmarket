package com.hanium.userservice.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinResponse {

    @ApiModelProperty(name = "시용자 아이디", notes = "사용자 아이디")
    private long userId;

    @ApiModelProperty(name = "시용자 별명", notes = "시용자 별명")
    private String nickname;
}
