package com.hanium.product.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductListResponse {

    @ApiModelProperty(name = "검색결과 리스트", notes = "검색결과(상품정보) 리스트")
    private List<ProductResponse> data;

    @ApiModelProperty(name = "검색결과 갯수", notes = "검색결과 갯수")
    private int count;

    @ApiModelProperty(name = "마지막 검색결과 아이디", notes = "마지막 검색결과 아이디")
    private Long lastProductId;

    public static ProductListResponse createResponse(List<ProductResponse> productResponseList) {
        return ProductListResponse.builder()
                .data(productResponseList)
                .lastProductId(productResponseList.size() > 0 ? Objects.requireNonNull(CollectionUtils.lastElement(productResponseList)).getId() : null)
                .count(productResponseList.size())
                .build();
    }
}
