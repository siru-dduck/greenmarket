package com.hanium.product.dao;

import java.util.List;

import com.hanium.product.dto.ProductArticleRequestDto;
import org.apache.ibatis.annotations.Delete;
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
    int createBy(@Param("productArticle") ProductArticleDto productArticle);

    @Update("UPDATE product_article " +
            "SET title = #{productArticle.title}," +
            "content = #{productArticle.content}," +
            "price = #{productArticle.price}," +
            "status = #{productArticle.status}," +
            "category_id = #{productArticle.categoryId}," +
            "update_date = now()" +
            "WHERE id = #{id} ")
    int updateBy(ProductArticleRequestDto productArticle, Integer id);

    @Delete("DELETE FROM product_article WHERE id = #{id}")
    int deleteBy(Integer id);

    @Update("UPDATE product_article SET interest_count = interest_count + 1  WHERE id = #{articleId}")
    int addInterestCount(Integer articleId);

    @Update("UPDATE product_article SET interest_count = interest_count - 1  WHERE id = #{articleId}")
    int subtractInterestCount(Integer articleId);
}
