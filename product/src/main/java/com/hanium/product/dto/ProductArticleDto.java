package com.hanium.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;


public class ProductArticleDto {
    private static final Integer DEFAULT_OFFSET_VALUE = 0;
    private static final Integer DEFAULT_LIMIT_VALUE = 20;

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

        public SearchInfo() {
            this.offset = DEFAULT_OFFSET_VALUE;
            this.limit = DEFAULT_LIMIT_VALUE;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegisterInfo {
        @NotEmpty
        private String title;
        @NotEmpty
        private String content;
        @Min(value = 0, message = "가격은 최소 0원이상 입력가능합니다.") @Max(value = 999999999, message = "가격은 1억원 미만까지 입력가능합니다.")
        private Integer price;
        private Integer categoryId;
        @NotEmpty(message = "파일은 최소 1개이상 첨부해야합니다.")
        @Size(max = 12,message = "파일은 최대 12개까지 첨부가능합니다.")
        private List<MultipartFile> files;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChangeInfo {
        @NotEmpty
        private String title;
        @NotEmpty
        private String content;
        @Min(value = 0, message = "가격은 최소 0원이상 입력가능합니다.") @Max(value = 999999999, message = "가격은 1억원 미만까지 입력가능합니다.")
        private Integer price;
        private Byte status;
        private Integer categoryId;
        @NotEmpty(message = "파일은 최소 1개이상 첨부해야합니다.")
        @Size(max = 12,message = "파일은 최대 12개까지 첨부가능합니다.")
        private List<MultipartFile> files;
    }
}