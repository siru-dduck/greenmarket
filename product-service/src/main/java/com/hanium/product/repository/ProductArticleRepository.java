package com.hanium.product.repository;

import com.hanium.product.domain.product.ProductArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Shin Woo Choi on 2021.10.10
 */
@Repository
public interface ProductArticleRepository extends JpaRepository<ProductArticle, Long>, ProductArticleCustomRepository {

    @Query("select p " +
            "from ProductArticle p " +
            "   left outer join ProductImage pi " +
            "       on p.id = pi.productArticle.id " +
            "where p.id = :productId ")
    ProductArticle findWithImageAndReviewById(@Param("productId") Long productId);
}
