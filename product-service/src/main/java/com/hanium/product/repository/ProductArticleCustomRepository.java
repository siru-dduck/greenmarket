package com.hanium.product.repository;

import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.dto.SearchInfoDto;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface ProductArticleCustomRepository {
    List<ProductArticle> findBySearchQuery(SearchInfoDto searchInfo);
}