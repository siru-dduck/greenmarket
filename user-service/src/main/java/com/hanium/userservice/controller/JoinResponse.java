package com.hanium.userservice.controller;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinResponse {
    private long userId;
}
