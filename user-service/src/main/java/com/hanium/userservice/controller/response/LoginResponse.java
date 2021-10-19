package com.hanium.userservice.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginResponse {

    @ApiModelProperty(name = "access token ", notes = "access token 유효기간 1시간")
    private String accessToken;

    @ApiModelProperty(name = "refresh token ", notes = "refresh token 유효기간 1주일")
    private String refreshToken;
}
