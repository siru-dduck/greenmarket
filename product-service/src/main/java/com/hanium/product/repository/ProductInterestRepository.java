package com.hanium.product.repository;

import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.ProductInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInterestRepository extends JpaRepository<ProductInterest, Long> {

    boolean existsByProductArticleIdAndUserId(long productId, long userId);
    void deleteByProductArticleEqualsAndUserId(ProductArticle product, long userId);
}