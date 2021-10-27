package com.hanium.product.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RegisterProductDto {
    private String title;
    private String content;
    private long userId;
    private int categoryId;
    private int price;
    private String address1;
    private String address2;
}
