package com.hanium.userservice.model;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetail {

    private long userId;
    private String email;
    private long profileImageId;

}