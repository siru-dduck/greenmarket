package com.hanium.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hanium.product.dto.ProductImageDto;

@Mapper
public interface IProductImageDao {
    void createList(List<ProductImageDto> productImages);

    @Delete("DELETE FROM product_image WHERE article_id = #{articleId}")
    void deleteBy(Integer articleId);
}
