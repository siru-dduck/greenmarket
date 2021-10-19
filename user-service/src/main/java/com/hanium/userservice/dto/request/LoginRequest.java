package com.hanium.userservice.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @ApiModelProperty(name = "이메일", notes = "이메일", required = true, example = "test@email.com")
    @NotBlank(message = "빈 문자열이 올 수 없습니다.")
    @Email(message = "이메일 유형에 맞지않습니다.")
    private String email;

    @ApiModelProperty(name = "비밀번호", notes = "비밀번호", required = true, example = "12345@")
    @NotBlank(message = "빈 문자열이 올 수 없습니다.")
    @Length(min = 6, max = 30, message = "비밀번호는 최소 6자이상 최대 30자 이하입니다.")
    private String password;

}
