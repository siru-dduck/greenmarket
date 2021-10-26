package com.hanium.product.dto.response;

import com.hanium.product.domain.product.ProductArticleStatus;
import com.hanium.product.dto.CategoryDto;
import com.hanium.product.dto.ProductImageDto;
import com.hanium.product.dto.user.UserInfoDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductResponse {
    private Integer id;
    private String title;
    private String content;
    private String address1;
    private String address2;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer price;
    private Integer interestCount;
    private ProductArticleStatus status;
    private CategoryDto category;
    private UserInfoDto user;
    private List<String> productImageUrlList;
}
