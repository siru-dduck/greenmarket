package com.hanium.product.dto;

import lombok.Data;

import java.util.List;

public class ChatRoomDto {
    @Data
    public static class Info {
        private Integer id;
        private Integer articleId;
        private Integer userIdBuyer;
        private Integer userIdSeller;
    }

    @Data
    public static class ResponseInfo {
        private boolean isSuccess;
        private List<Info> chatRoom;
    }
}
