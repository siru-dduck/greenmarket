package com.hanium.product.repository;

import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.QProductArticle;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;

/**
 * @author Shin Woo Choi on 2021.10.10
 */
@Repository
public interface ProductArticleRepository extends JpaRepository<ProductArticle, Long>,
        QuerydslPredicateExecutor<ProductArticle>, QuerydslBinderCustomizer<QProductArticle> {
    @Override
    default void customize(QuerydslBindings bindings, QProductArticle root) {
        bindings.bind(String.class)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }
}
