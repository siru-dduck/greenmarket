package com.hanium.product.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SearchRequest {

    @ApiModelProperty(name = "키워드", notes = "게시글 제목, 내용에 포함된 키워드")
    @Length(min = 2, max = 30, message = "키워드는 최소 2자이상 최대30자까지 가능합니다.")
    private String keyword;

    @ApiModelProperty(name = "검색 필터", notes = "검색 필터 rest query { =(equal), ~(like), >(greater_than)" +
            ", <(less_than), [(startsWith), ](endsWith), [](contains)}")
    @Length(max = 100, message = "필터의 길이는 100자 이상 초과할 수 없습니다.")
    private String filter;

    @ApiModelProperty(name = "오프셋", notes = "오프셋: 기준-상품아이디")
    @Range(min = 0, message = "오프셋은 0이상이어야 합니다.")
    private Long offset;

    @ApiModelProperty(name = "페이지 사이즈", notes = "페이지 사이즈")
    @Range(min = 1, max = 100, message = "페이지 사이즈는 1이상 100이하 입니다.")
    private Integer size;

}
