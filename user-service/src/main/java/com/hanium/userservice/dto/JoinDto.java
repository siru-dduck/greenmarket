package com.hanium.userservice.dto;

import lombok.Data;

@Data
public class JoinDto {
    private String email;
    private String password;
    private String address1;
    private String address2;
    private String nickname;
}
