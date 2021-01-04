package com.hanium.product.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductArticleRequestDto {
    private String title;
    private String content;
    private Integer price;
    private Byte status;
    private Integer categoryId;
    private List<MultipartFile> files;
}
