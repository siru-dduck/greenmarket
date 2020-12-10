package com.hanium.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hanium.product.dto.ProductImageDto;

@Mapper
public interface IProductImageDao {
	public List<ProductImageDto> findList(@Param("articleId") Integer articleId);
	public int createList(@Param("productImages") List<ProductImageDto> productImages);
}
