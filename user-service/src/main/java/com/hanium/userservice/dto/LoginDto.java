package com.hanium.userservice.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class LoginDto {
    private String email;
    private String password;
}
