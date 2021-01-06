package com.hanium.product.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDto {
	private Integer articleId;
	private Integer listNum;
	private String fileUrl;
}
