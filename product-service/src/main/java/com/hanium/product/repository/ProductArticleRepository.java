package com.hanium.product.repository;

import com.hanium.product.domain.product.ProductArticle;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Shin Woo Choi on 2021.10.10
 */
@Repository
public class ProductArticleRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(ProductArticle productArticle) {
        em.persist(productArticle);
        return productArticle.getId();
    }

    public ProductArticle findById(Long id) {
        return em.find(ProductArticle.class, id);
    }
}
