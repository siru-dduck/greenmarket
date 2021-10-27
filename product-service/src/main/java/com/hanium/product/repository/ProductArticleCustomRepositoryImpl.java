package com.hanium.product.repository;

import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.QProductArticle;
import com.hanium.product.domain.product.QProductImage;
import com.hanium.product.dto.SearchInfoDto;
import com.hanium.product.repository.querydsl.ProductArticlePredicatesBuilder;
import com.hanium.product.repository.querydsl.SearchOperation;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional(readOnly = true)
public class ProductArticleCustomRepositoryImpl extends QuerydslRepositorySupport implements ProductArticleCustomRepository {

    public ProductArticleCustomRepositoryImpl() {
        super(ProductArticle.class);
    }

    @Override
    public List<ProductArticle> findBySearchQuery(SearchInfoDto searchInfo) {
        ProductArticlePredicatesBuilder builder = new ProductArticlePredicatesBuilder();
        builder.with(searchInfo.getOffset())
                .with(searchInfo.getKeyword());

        String filter = searchInfo.getFilter();
        if (StringUtils.isNotBlank(filter)) {
            Pattern pattern = Pattern.compile("([\\w|.]+?)(=|~|<|>|\\[|]|\\[])([ㄱ-ㅎ|ㅏ-ㅣ가-힣\\w]+?),");
            Matcher matcher = pattern.matcher(filter + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), SearchOperation.getSimpleOperation(matcher.group(2)), matcher.group(3));
            }
        }

        final QProductArticle productArticle = QProductArticle.productArticle;
        final QProductImage productImage = QProductImage.productImage;
        return from(productArticle)
                .where(builder.build())
                .orderBy(productArticle.createDate.desc())
                .limit(searchInfo.getSize())
                .fetch();
    }
}
