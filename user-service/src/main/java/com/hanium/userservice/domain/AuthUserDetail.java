package com.hanium.userservice.domain;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserDetail {

    private long userId;
    private String email;
    private Long profileImageId;
    private String tokenId;

}