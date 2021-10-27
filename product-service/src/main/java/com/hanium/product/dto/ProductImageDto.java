package com.hanium.product.dto;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductImageDto {
	private long articleId;
	private int listNum;
	private long fileId;
}
