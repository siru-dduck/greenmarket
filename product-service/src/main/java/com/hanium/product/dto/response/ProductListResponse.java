package com.hanium.product.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductListResponse {

    @ApiModelProperty(name = "검색결과 리스트", notes = "검색결과(상품정보) 리스트")
    private List<ProductResponse> result;

    @ApiModelProperty(name = "검색결과 길이", notes = "검색결과 길이")
    private int length;

    @ApiModelProperty(name = "마지막 검색결과 아이디", notes = "마지막 검색결과 아이디")
    private Long lastProductId;
}
