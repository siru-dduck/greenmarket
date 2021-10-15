package com.hanium.product.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	@Builder
	public static class Info {
		private Integer id;
		private String address1;
		private String address2;
		private String nickname;
		private String profileImageUrl;
	}

	@Data
	@NoArgsConstructor
	public static class ResponseInfo {
		private boolean isSuccess;
		private UserDto.Info user;
	}
}
