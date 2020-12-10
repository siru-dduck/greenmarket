package com.hanium.product.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductArticleDto {
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
