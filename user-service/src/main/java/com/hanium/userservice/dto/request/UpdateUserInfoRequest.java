package com.hanium.userservice.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateUserInfoRequest {

    @ApiModelProperty(name = "주소1(시/군)", notes = "주소1(시/군)")
    private String address1;

    @ApiModelProperty(name = "주소2(구/읍)", notes = "주소2(구/읍)")
    private String address2;

    @ApiModelProperty(name = "사용자 별명", notes = "사용자 별명")
    private String nickname;

    @ApiModelProperty(name = "사용자 프로필 이미지 파일 아이디", notes = "사용자 프로필 이미지 파일 아이디")
    private Long profileFileId;
}
