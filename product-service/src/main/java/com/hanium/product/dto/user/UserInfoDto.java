package com.hanium.product.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class UserInfoDto {
    private long userId;
    private String email;
    private String nickname;
    private String address1;
    private String address2;
    private Long profileFileId;
    private LocalDateTime creatDate;
}