package com.hanium.product.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductArticlePredicatesBuilder {

    private List<SearchCriteria> params;
    private String keyword;
    private Long offset;

    public ProductArticlePredicatesBuilder() {
        this.params = new ArrayList<>();
    }

    public ProductArticlePredicatesBuilder with(
            String key, SearchOperation operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public ProductArticlePredicatesBuilder with(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public ProductArticlePredicatesBuilder with(Long offset) {
        this.offset = offset;
        return this;
    }

    public BooleanExpression build() {
        if (params.size() == 0) {
            return null;
        }

        List<BooleanExpression> predicates = params.stream()
                .map(ProductArticlePredicate::getPredicate)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        // 필터
        for (BooleanExpression predicate : predicates) {
            result = result.and(predicate);
        }

        // 검색키워드 포함 (title, content)
        if(keyword != null) {
            result = result.and(ProductArticlePredicate.getPredicate(this.keyword));
        }

        // 오프셋 (상품 게시글 키값 기준)
        if (offset != null) {
            result = result.and(ProductArticlePredicate.getPredicate(
                    new SearchCriteria("id", SearchOperation.GREATER_THAN, offset)
            ));
        }
        return result;
    }

}
