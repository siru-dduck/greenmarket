package com.hanium.userservice.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JoinResponse {

    @ApiModelProperty(name = "시용자 아이디", notes = "사용자 아이디")
    private long userId;

    @ApiModelProperty(name = "시용자 별명", notes = "시용자 별명")
    private String nickname;
}