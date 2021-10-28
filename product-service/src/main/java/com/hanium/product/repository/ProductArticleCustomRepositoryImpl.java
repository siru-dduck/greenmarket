package com.hanium.product.repository;

import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.QCategory;
import com.hanium.product.dto.FindProductListDto;
import com.hanium.product.dto.SearchInfoDto;
import com.hanium.product.repository.querydsl.ProductArticlePredicatesBuilder;
import com.hanium.product.repository.querydsl.SearchOperation;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hanium.product.domain.product.QProductArticle.productArticle;

@Transactional(readOnly = true)
public class ProductArticleCustomRepositoryImpl extends QuerydslRepositorySupport implements ProductArticleCustomRepository {

    public ProductArticleCustomRepositoryImpl() {
        super(ProductArticle.class);
    }

    /**
     * 상품검색
     * @param searchInfo
     * @return
     */
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

        return from(productArticle)
                .innerJoin(productArticle.category).fetchJoin()
                .where(builder.build())
                .orderBy(productArticle.createDate.desc())
                .limit(searchInfo.getSize())
                .fetch();
    }

    /**
     * 상품 리스트 조회
     * @param findProductListInfo
     * @return
     */
    @Override
    public List<ProductArticle> findListBy(FindProductListDto findProductListInfo) {
        Long userId = findProductListInfo.getUserId();
        List<Long> productIdList = findProductListInfo.getProductIdList();

        return from(productArticle)
                .innerJoin(productArticle.category).fetchJoin()
                .where(eqUserId(userId),
                        inProductIdList(productIdList))
                .orderBy(productArticle.id.asc())
                .fetch();
    }

    private BooleanExpression eqUserId(Long userId) {
        if(userId == null) {
            return null;
        }
        return productArticle.userId.eq(userId);
    }

    private BooleanExpression inProductIdList(List<Long> productIdList) {
        if(CollectionUtils.isEmpty(productIdList)) {
            return null;
        }
        return productArticle.id.in(productIdList);
    }

}
