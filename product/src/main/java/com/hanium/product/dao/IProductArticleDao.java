package com.hanium.product.dao;

import java.util.List;

import com.hanium.product.dto.ProductArticleRequestDto;
import lombok.ToString;
import org.apache.ibatis.annotations.*;

import com.hanium.product.dto.ProductArticleDto;

@Mapper
public interface IProductArticleDao {
    List<ProductArticleDto.Info> findList(
            String keyword,
            String address1,
            String address2,
            Integer userId,
            String order,
            Integer offset,
            Integer limit,
            List<Integer> articleIds);

    ProductArticleDto.Info findBy(Integer id);

    @Insert("INSERT " +
            "INTO product_article(title, content, write_date, price, user_id, category_id)" +
            "values(#{title}," +
            "#{content}," +
            "now()," +
            "#{price}," +
            "#{user.id}," +
            "#{category.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createBy(ProductArticleDto.Info productArticle);

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
    void addInterestCount(Integer articleId);

    @Update("UPDATE product_article SET interest_count = interest_count - 1  WHERE id = #{articleId}")
    void subtractInterestCount(Integer articleId);
}
