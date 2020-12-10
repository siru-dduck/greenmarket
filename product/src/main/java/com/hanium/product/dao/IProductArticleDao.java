package com.hanium.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hanium.product.dto.ProductArticleDto;

@Mapper
public interface IProductArticleDao {
	List<ProductArticleDto> findList(@Param("keyword") String keyword,
			@Param("address1") String address1, 
			@Param("address2") String address2,
			@Param("userId") Integer userId,
			@Param("interestCount") String interestCount,
			@Param("offset") Integer offset,
			@Param("limit") Integer limit,
			@Param("articleIds") List<Integer> articleIds);
	ProductArticleDto findBy(@Param("id") Integer id);
	Integer create(@Param("productArticle") ProductArticleDto productArticle);
}
