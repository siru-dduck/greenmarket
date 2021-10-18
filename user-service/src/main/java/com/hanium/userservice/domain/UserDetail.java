package com.hanium.userservice.domain;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetail {

    private long userId;
    private String email;
    private Long profileImageId;
    private String tokenId;

}