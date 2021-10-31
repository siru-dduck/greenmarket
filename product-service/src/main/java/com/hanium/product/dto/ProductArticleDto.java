package com.hanium.product.dto;

import com.hanium.product.domain.product.ProductStatus;
import com.hanium.product.dto.user.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private ProductStatus status;
    private CategoryDto category;
    private UserInfoDto user;
    private long userId;
    private List<ProductImageDto> productImageList;
}