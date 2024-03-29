package com.hanium.product.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hanium.product.domain.product.ProductStatus;
import com.hanium.product.dto.AddressDto;
import com.hanium.product.dto.CategoryDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductResponse {

    @ApiModelProperty(name = "상품 아이디", notes = "상품 아이디")
    private long id;

    @ApiModelProperty(name = "상품 제목", notes = "상품 제목")
    private String title;

    @ApiModelProperty(name = "상품 내용", notes = "상품 내용")
    private String content;

    @ApiModelProperty(name = "상품 거래주소", notes = "상품 거래주소")
    private AddressDto address;

    @ApiModelProperty(name = "상품 등록일", notes = "상품 등록일")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @ApiModelProperty(name = "상품 수정일", notes = "상품 수정일")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    @ApiModelProperty(name = "상품 가격", notes = "상품 가갹(0원인 경우 나눔상품)")
    private int price;

    @ApiModelProperty(name = "상품 관심수", notes = "상품 관심수")
    private int interestCount;

    @ApiModelProperty(name = "상품상태", notes = "상품상태[SALE(판매중)" +
            ", RESERVE(예약중), COMPLETE(거래완료)" +
            ", HIDE(숨김), DELETE(삭제)]")
    private ProductStatus status;

    @ApiModelProperty(name = "상품 카테고리", notes = "상품 카테고리")
    private CategoryDto category;

    @ApiModelProperty(name = "상품 등록자 아이디", notes = "상품 등록자 아이디")
    private long userId;

    @ApiModelProperty(name = "상품 이미지파일 아이디 리스트", notes = "상품 이미지파일 아이디 리스트")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> imageFileIdList;

    @ApiModelProperty(name = "좋아요 체크 여부", notes = "좋아요 체크 여부")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isCheckInterest;
}
