package com.hanium.product.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IProductInterestDao {
    @Select("SELECT count(*)\n" +
            "FROM product_interest\n"+
            "WHERE article_id = #{articleId} and user_id = #{userId}")
    int check(Integer articleId, Integer userId);

    @Select("SELECT count(*)\n" +
            "FROM product_interest\n" +
            "WHERE article_id = #{articleId}")
    int count(Integer articleId);

    @Insert("INSERT\n" +
            "INTO product_interest(article_id, user_id)\n" +
            "VALUES(#{articleId}, #{userId})")
    int create(Integer articleId, Integer userId);

    @Delete("DELETE\n" +
            "FROM product_interest\n" +
            "WHERE article_id = #{articleId} and user_id = #{userId}")
    int delete(Integer articleId, Integer userId);
}
