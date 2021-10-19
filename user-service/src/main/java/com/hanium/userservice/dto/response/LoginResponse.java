package com.hanium.userservice.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {

    @ApiModelProperty(name = "access token ", notes = "access token 유효기간 1시간")
    private String accessToken;

    @ApiModelProperty(name = "refresh token ", notes = "refresh token 유효기간 1주일")
    private String refreshToken;
}
