package com.hanium.product.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateProductRequest {

    @ApiModelProperty(name = "글 제목", notes = "상품 글 제목")
    @NotBlank(message = "글 내용은 반드시 입력해야합니다.")
    @Length(max = 150, message = "글제목은 150자까지 입력가능합니다.")
    private String title;

    @ApiModelProperty(name = "글 내용", notes = "상품 글 내용")
    @NotBlank(message = "글 내용은 반드시 입력해야합니다.")
    @Length(max = 500, message = "글 내용은 최대 500자까지 입력가능합니다.")
    private String content;

    @ApiModelProperty(name = "상품가격", notes = "상품가격")
    @NotNull(message = "가격을 입력해야합니다.")
    @Min(value = 0, message = "가격은 최소 0원이상 입력가능합니다.") @Max(value = 999999999, message = "가격은 1억원 미만까지 입력가능합니다.")
    private Integer price;

    @ApiModelProperty(name = "카테고리 아이디", notes = "카테고리 아이디")
    @NotNull(message = "카테고리 아이디를 반드시 입력해야합니다.")
    private Integer categoryId;

    @ApiModelProperty(name = "주소1(시,군)", notes = "주소1(시,군)")
    @NotBlank(message = "주소는 반드시 입력해야합니다.")
    private String address1;

    @ApiModelProperty(name = "주소2(구,읍)", notes = "주소2(구,읍)")
    @NotBlank(message = "주소는 반드시 입력해야합니다.")
    private String address2;

    @ApiModelProperty(name = "첨부 이미지 파일 아이디", notes = "첨부 이미지 파일 아이디")
    @NotEmpty(message = "파일은 최소 1개이상 등록해야합니다.")
    @Size(max = 12, message = "파일은 최대 12개까지 첨부가능합니다.")
    private List<Long> fileIdList;

}

