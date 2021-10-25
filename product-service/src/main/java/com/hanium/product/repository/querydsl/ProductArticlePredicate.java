package com.hanium.product.repository.querydsl;

import com.hanium.product.domain.product.ProductArticle;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import lombok.AllArgsConstructor;

import static org.apache.commons.lang3.StringUtils.isNumeric;

@AllArgsConstructor
public class ProductArticlePredicate {

    private SearchCriteria criteria;

    public BooleanExpression getPredicate() {
        PathBuilder<ProductArticle> entityPath = new PathBuilder<>(ProductArticle.class, "productArticle");

        if (isNumeric(criteria.getValue().toString())) {
            NumberPath<Integer> path = entityPath.getNumber(criteria.getKey(), Integer.class);
            int value = Integer.parseInt(criteria.getValue().toString());
            switch (criteria.getOperation()) {
                case ":":
                    return path.eq(value);
                case ">":
                    return path.goe(value);
                case "<":
                    return path.loe(value);
            }
        }
        else {
            StringPath path = entityPath.getString(criteria.getKey());
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.containsIgnoreCase(criteria.getValue().toString());
            }
        }
        return null;
    }

}
