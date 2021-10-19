package com.hanium.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResultDto {
    private String accessToken;
    private String refreshToken;
}
