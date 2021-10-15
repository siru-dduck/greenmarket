package com.hanium.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.hanium.product.dto.ProductArticleDto;

@Mapper
public interface IProductArticleDao {
    List<ProductArticleDto.Info> findListBy(
            ProductArticleDto.SearchInfo searchInfo);

    ProductArticleDto.Info findOneBy(Integer id);

    @Insert("INSERT " +
            "INTO product_article(title, content, write_date, price, user_id, address1, address2, category_id)" +
            "values(#{title}," +
            "#{content}," +
            "now()," +
            "#{price}," +
            "#{user.id}," +
            "#{address1}," +
            "#{address2}," +
            "#{category.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createBy(ProductArticleDto.Info productArticle);

    @Update("UPDATE product_article " +
            "SET title = #{productArticle.title}," +
            "content = #{productArticle.content}," +
            "price = #{productArticle.price}," +
            "status = #{productArticle.status}," +
            "category_id = #{productArticle.categoryId}," +
            "address1 = #{productArticle.address1}," +
            "address2 = #{productArticle.address2}," +
            "update_date = now()" +
            "WHERE id = #{id} ")
    int updateBy(ProductArticleDto.ChangeInfo productArticle, Integer id);

    @Delete("DELETE FROM product_article WHERE id = #{id}")
    int deleteBy(Integer id);

    @Update("UPDATE product_article SET interest_count = interest_count + 1  WHERE id = #{articleId}")
    void addInterestCount(Integer articleId);

    @Update("UPDATE product_article SET interest_count = interest_count - 1  WHERE id = #{articleId}")
    void subtractInterestCount(Integer articleId);
}
