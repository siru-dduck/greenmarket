package com.hanium.userservice.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AuthUserInfoResponse {

    @ApiModelProperty(name = "사용자 아이디", notes = "사용자 아이디")
    private long userId;

    @ApiModelProperty(name = "사용자 이메일", notes = "사용자 이메일")
    private String email;

    @ApiModelProperty(name = "사용자 별명", notes = "사용자 별명")
    private String nickname;

    @ApiModelProperty(name = "사용자 프로필 이미지 파일 아이디", notes = "사용자 프로필 이미지 파일 아이디")
    private Long profileImageId;

    @ApiModelProperty(name = "jwt 토큰 아이디", notes = "jwt 토큰 아이디")
    private String tokenId;
}
