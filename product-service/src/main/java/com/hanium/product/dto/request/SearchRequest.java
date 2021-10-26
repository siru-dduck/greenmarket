package com.hanium.product.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SearchRequest {

    @Length(min = 2, max = 30, message = "키워드는 최소 2자이상 최대30자까지 가능합니다.")
    private String keyword;

    @Length(max = 100, message = "필터의 길이는 100자 이상 초과할 수 없습니다.")
    private String filter;

    @Range(min = 0, message = "오프셋은 0이상이어야 합니다.")
    private Long offset;

    @Range(min = 1, max = 100, message = "페이지 사이즈는 1이상 100이하 입니다.")
    private Integer size;

}
