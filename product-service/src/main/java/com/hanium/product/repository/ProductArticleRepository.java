package com.hanium.product.repository;

import com.hanium.product.domain.product.ProductArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Shin Woo Choi on 2021.10.10
 */
@Repository
public interface ProductArticleRepository extends JpaRepository<ProductArticle, Long>, ProductArticleCustomRepository {
}
