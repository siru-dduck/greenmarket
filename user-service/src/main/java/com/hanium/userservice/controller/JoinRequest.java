package com.hanium.userservice.controller;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class JoinRequest {

    @NotBlank(message = "빈 문자열이 올 수 없습니다.")
    @Email(message = "이메일 유형에 맞지않습니다.")
    private String email;

    @NotBlank(message = "빈 문자열이 올 수 없습니다.")
    @Range(min = 6, max = 30, message = "비밀번호는 최소 6자이상 최대30자 이하입니다.")
    private String password;


}
