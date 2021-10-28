package com.hanium.product.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProductListRequest {

    @ApiModelProperty(name = "상품 등록자 아이디", notes = "상품 등록자 아이디")
    private Long userId;

    @ApiModelProperty(name = "상품 아이디 리스트", notes = "상품 아이디 리스트")
    private List<Long> productIdList;

}
