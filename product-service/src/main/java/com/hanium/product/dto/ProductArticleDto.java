package com.hanium.product.dto;

import com.hanium.product.domain.product.ProductArticleStatus;
import com.hanium.product.dto.user.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductArticleDto {

    private Integer id;
    private String title;
    private String content;
    private AddressDto address;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private int price;
    private int interestCount;
    private ProductArticleStatus status;
    private CategoryDto category;
    private UserInfoDto user;
    private long userId;
    private List<ProductImageDto> productImageList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Info {
        private Integer id;
        private String title;
        private String content;
        private String address1;
        private String address2;
        private LocalDateTime writeDate;
        private LocalDateTime updateDate;
        private Integer price;
        private Integer interestCount;
        private Byte status;
        private CategoryDto category;
        private UserDto.Info user;
        private List<ProductImageDto> productImages;
    }

    @Data
    @AllArgsConstructor
    public static class SearchInfo {
        @Length(min = 2, max = 30, message = "검색 키워드는 2자 이상, 30자 이하로 입력하세요.")
        private String keyword;
        private String address1;
        private String address2;
        private Integer userId;
        private String order;
        private Integer offset;
        @Min(value = 1, message = "limit은 1이상의 정수로 입력해야합니다.")
        private Integer limit;
        private List<String> articleIds;
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
        @NotNull
        @Min(value = 0, message = "가격은 최소 0원이상 입력가능합니다.") @Max(value = 999999999, message = "가격은 1억원 미만까지 입력가능합니다.")
        private Integer price;
        @NotNull
        private Byte status;
        @NotNull
        private Integer categoryId;
        @NotEmpty
        private String address1;
        @NotEmpty
        private String address2;
        @NotEmpty(message = "파일은 최소 1개이상 첨부해야합니다.")
        @Size(max = 12,message = "파일은 최대 12개까지 첨부가능합니다.")
        private List<MultipartFile> files;
    }
}