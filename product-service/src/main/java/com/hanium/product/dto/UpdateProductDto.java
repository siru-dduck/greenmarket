package com.hanium.product.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UpdateProductDto {
    private String title;
    private String content;
    private int categoryId;
    private int price;
    private String address1;
    private String address2;
    private List<Long> fileIdList;
}
