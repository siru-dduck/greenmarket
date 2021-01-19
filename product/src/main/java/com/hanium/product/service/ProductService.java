package com.hanium.product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.ProductImageDto;

public interface ProductService {
    List<ProductArticleDto.Info> getProductArticles(ProductArticleDto.SearchInfo searchInfo);

    ProductArticleDto.Info getProductArticle(Integer articleId);

    Integer createProductArticle(ProductArticleDto.RegisterInfo registerInfo, Integer userId);

    void updateProductArticle(ProductArticleDto.ChangeInfo changeInfo, Integer articleId, Integer userId);

    void deleteProductArticle(Integer articleId, Integer userId);
}
