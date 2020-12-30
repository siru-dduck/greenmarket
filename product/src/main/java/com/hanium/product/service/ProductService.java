package com.hanium.product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.ProductImageDto;

public interface ProductService {
	List<ProductArticleDto> getProductArticles(String keyword,
			String address1, 
			String address2,
			Integer userId,
			String order,
			Integer offset,
			Integer limit,
			List<Integer> articleIds);
	ProductArticleDto getProductArticle(Integer articleId);
	List<ProductImageDto> getProductImages(Integer articleId);
	Integer createProductArticle(ProductArticleDto productArticle,
			List<MultipartFile> multipartFiles) throws Exception;
	Integer deleteProductArticle(Integer id);
}
