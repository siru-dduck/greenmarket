package com.hanium.product.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SearchInfoDto {
    private String keyword;
    private String filter;
    private long offset;
    private int size;
}
