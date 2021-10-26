package com.hanium.product.repository.querydsl;

import com.hanium.product.domain.product.ProductArticle;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;

import static org.apache.commons.lang3.StringUtils.isNumeric;

public class ProductArticlePredicate {

    public static BooleanExpression getPredicate(SearchCriteria criteria) {
        PathBuilder<ProductArticle> entityPath = new PathBuilder<>(ProductArticle.class, "productArticle");

        if (criteria.getOperation() == null) {
            return null;
        }

        if (isNumeric(criteria.getValue().toString())) {
            NumberPath<Integer> path = entityPath.getNumber(criteria.getKey(), Integer.class);
            int value = Integer.parseInt(criteria.getValue().toString());
            switch (criteria.getOperation()) {
                case EQUALITY:
                    return path.eq(value);
                case GREATER_THAN:
                    return path.goe(value);
                case LESS_THAN:
                    return path.loe(value);
            }
        } else {
            StringPath path = entityPath.getString(criteria.getKey());
            switch (criteria.getOperation()) {
                case LIKE:
                    return path.like(criteria.getValue().toString());
                case CONTAINS:
                    return path.contains(criteria.getValue().toString());
                case STARTS_WITH:
                    return path.startsWith(criteria.getValue().toString());
                case ENDS_WITH:
                    return path.endsWith(criteria.getValue().toString());
            }
        }
        return null;
    }

    public static BooleanExpression getPredicate(String keyword) {
        if(keyword == null) {
            return null;
        }

        PathBuilder<ProductArticle> entityPath = new PathBuilder<>(ProductArticle.class, "productArticle");
        StringPath titlePath = entityPath.getString("title");
        StringPath contentPath = entityPath.getString("content");
        return titlePath.contains(keyword).or(contentPath.contains(keyword));
    }
}