package com.hanium.product.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ProductListResponse {
    private List<ProductResponse> result;
}
