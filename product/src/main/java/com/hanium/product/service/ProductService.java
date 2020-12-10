package com.hanium.product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.ProductImageDto;

public interface ProductService {
	public List<ProductArticleDto> getProductArticles(String keyword,
			String address1, 
			String address2,
			Integer userId,
			String interestCount,
			Integer offset,
			Integer limit,
			List<Integer> articleIds);
	public ProductArticleDto getProductArticle(Integer articleId);
	public List<ProductImageDto> getProductImages(Integer articleId);
	public Integer writeProductArticle(ProductArticleDto productArticle,
			List<MultipartFile> multipartFiles) throws Exception; 
}
