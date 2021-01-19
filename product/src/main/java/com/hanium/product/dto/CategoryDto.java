package com.hanium.product.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
	private Integer id;
	private String name;
}
