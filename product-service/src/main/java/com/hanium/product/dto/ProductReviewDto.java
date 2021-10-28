package com.hanium.product.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductReviewDto {
    private Long id;
    private String content;
    private Long userid;
    private LocalDateTime createDate;
}
