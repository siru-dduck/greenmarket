package com.hanium.product.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SearchRequest {

    @Length(min = 2, max = 30, message = "키워드는 최소 2자이상 최대30자까지 가능합니다.")
    private String keyword;

    @Length(max = 100, message = "filter의 길이는 100자 이상 초과할 수 없습니다.")
    private String filter;

}
