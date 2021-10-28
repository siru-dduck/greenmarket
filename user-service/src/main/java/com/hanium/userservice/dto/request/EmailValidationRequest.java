package com.hanium.userservice.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmailValidationRequest {

    @ApiModelProperty(name = "이메일", notes = "이메일 형식, 빈 문자열x", example = "test@example.com")
    @NotEmpty(message = "문자열은 빈 문자열이 올 수 없습니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
}
