package com.hanium.product.repository;

import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.dto.FindProductListDto;
import com.hanium.product.dto.SearchInfoDto;

import java.util.List;

public interface ProductArticleCustomRepository {
    List<ProductArticle> findBySearchQuery(SearchInfoDto searchInfo);

    List<ProductArticle> findListBy(FindProductListDto findProductListInfo);
}