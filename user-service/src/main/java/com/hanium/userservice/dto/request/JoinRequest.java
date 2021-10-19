package com.hanium.userservice.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class JoinRequest {

    @ApiModelProperty(name = "이메일", notes = "이메일", required = true, example = "test@email.com")
    @NotBlank(message = "빈 문자열이 올 수 없습니다.")
    @Email(message = "이메일 유형에 맞지않습니다.")
    private String email;

    @ApiModelProperty(name = "비밀번호", notes = "비밀번호 6~30자", required = true, example = "12345@")
    @NotBlank(message = "빈 문자열이 올 수 없습니다.")
    @Length(min = 6, max = 30, message = "비밀번호는 최소 6자이상 최대 30자 이하입니다.")
    private String password;

    @ApiModelProperty(name = "주소1(시,군)", notes = "주소1(시,군)", required = true, example = "서울특별시")
    @NotBlank(message = "빈 문자열이 올 수 없습니다.")
    @Length(max = 30, message = "주소란은 30자 이하입니다.")
    private String address1;

    @ApiModelProperty(name = "주소2(구,읍)", notes = "주소2(구,읍)", required = true, example = "강남구")
    @NotBlank(message = "빈 문자열이 올 수 없습니다.")
    @Length(max = 30, message = "주소란은 30자 이하입니다.")
    private String address2;

    @ApiModelProperty(name = "별명", notes = "별명 최대 30자", example = "test nickname#@!")
    @NotBlank(message = "빈 문자열이 올 수 없습니다.")
    @Length(max = 30, message = "닉네임은 30자 이하입니다.")
    private String nickname;
}
