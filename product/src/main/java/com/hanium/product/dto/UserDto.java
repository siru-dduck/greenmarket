package com.hanium.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
