package com.hanium.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hanium.product.dto.ProductArticleDto;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface IProductArticleDao {
    List<ProductArticleDto> findList(
            String keyword,
            String address1,
            String address2,
            Integer userId,
            String order,
            Integer offset,
            Integer limit,
            List<Integer> articleIds);

    ProductArticleDto findBy(Integer id);

    int create(@Param("productArticle") ProductArticleDto productArticle);

    @Update("UPDATE product_article SET interest_count = interest_count + 1  WHERE id = #{articleId}")
    int addInterestCount(Integer articleId);

    @Update("UPDATE product_article SET interest_count = interest_count - 1  WHERE id = #{articleId}")
    int subtractInterestCount(Integer articleId);
}
