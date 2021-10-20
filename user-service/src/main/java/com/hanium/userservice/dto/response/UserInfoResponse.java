package com.hanium.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class UserInfoResponse {

    @ApiModelProperty(name = "사용자 아이디", notes = "사용자 아이디")
    private long userId;

    @ApiModelProperty(name = "사용자 닉네임", notes = "사용자 닉네임")
    private String nickname;

    @ApiModelProperty(name = "사용자 주소1(시/군)", notes = "사용자 주소1(시/군)")
    private String address1;

    @ApiModelProperty(name = "사용자 주소2(구/읍)", notes = "사용자 주소2(구/읍)")
    private String address2;

    @ApiModelProperty(name = "사용자 프로필 아이디", notes = "사용자 프로필 아이디")
    private Long profileFileId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "사용자 가입일", notes = "가입일")
    private LocalDateTime creatDate;
}
