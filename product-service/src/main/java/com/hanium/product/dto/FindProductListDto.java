package com.hanium.product.dto;

import lombok.Data;

import java.util.List;

@Data
public class FindProductListDto {
    private Long userId;
    private List<Long> productIdList;
}