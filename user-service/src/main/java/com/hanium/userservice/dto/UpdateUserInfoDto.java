package com.hanium.userservice.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateUserInfoDto {
    private long userId;
    private String address1;
    private String address2;
    private String nickname;
    private Long profileFileId;
}
