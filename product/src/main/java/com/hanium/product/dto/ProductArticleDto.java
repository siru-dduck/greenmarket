package com.hanium.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


public class ProductArticleDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Info {
        private Integer id;
        private String title;
        private String content;
        private LocalDateTime writeDate;
        private LocalDateTime updateDate;
        private Integer price;
        private Integer interestCount;
        private Byte status;
        private CategoryDto category;
        private UserDto user;
        private String mainImageUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchInfo {
        @Length(min = 2, max = 30, message = "검색 키워드는 2자 이상, 20자 이하로 입력하세요.")
        private String keyword;
        private String address1;
        private String address2;
        private Integer userId;
        private String order;
        @NotNull
        @Min(value = 0, message = "offset은 0이상의 정수로 입력해야합니다.")
        private Integer offset;
        @NotNull
        @Min(value = 1, message = "limit은 1이상의 정수로 입력해야합니다.")
        private Integer limit;
        private List<Integer> articleIds;
    }
}

