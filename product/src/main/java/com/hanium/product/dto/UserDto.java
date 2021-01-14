package com.hanium.product.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
	private Integer id;
	private String address1;
	private String address2;
	private String nickname;
	private String profileImageUrl;
}
