package com.hanium.product.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RegisterProductResponse {

    @ApiModelProperty(name = "등록 상품 아이디", notes = "등록 상품 아이디")
    private long productId;

    @ApiModelProperty(name = "등록 상품 url link", notes = "등록 상품 url link")
    private String link;
}
