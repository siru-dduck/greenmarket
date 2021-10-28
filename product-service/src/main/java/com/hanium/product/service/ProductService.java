package com.hanium.product.service;

import com.hanium.product.dao.IProductArticleDao;
import com.hanium.product.dao.IProductImageDao;
import com.hanium.product.domain.product.Category;
import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.ProductImage;
import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.RegisterProductDto;
import com.hanium.product.dto.SearchInfoDto;
import com.hanium.product.dto.mapper.ProductArticleMapper;
import com.hanium.product.exception.InvalidCategoryIdException;
import com.hanium.product.exception.ProductNotFoundException;
import com.hanium.product.repository.CategoryRepository;
import com.hanium.product.repository.ProductArticleImageRepository;
import com.hanium.product.repository.ProductArticleRepository;
import com.hanium.product.repository.ProductInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author siru
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductArticleRepository productArticleRepository;
    private final ProductArticleImageRepository productArticleImageRepository;
    private final ProductArticleMapper productArticleMapper;
    private final CategoryRepository categoryRepository;
    private final ProductInterestRepository productInterestRepository;

    /*************************************************
     * 상품 관련 비즈니스 로직
     *************************************************/

    /**
     * 상품검색
     * @param searchInfo
     * @return
     */
    public List<ProductArticleDto> searchProductArticles(SearchInfoDto searchInfo) {
        List<ProductArticle> productArticleList = productArticleRepository.findBySearchQuery(searchInfo);
        Map<ProductArticle, ProductImage> productMainImageMap = productArticleImageRepository.findMainImageByFileIdIn(
                productArticleList.stream()
                        .map(ProductArticle::getId)
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(ProductImage::getProductArticle, Function.identity()));

        List<ProductArticleDto> searchResult = new ArrayList<>();
        productArticleList.forEach(productArticle -> {
            ProductArticleDto productArticleInfo = productArticleMapper.map(productArticle);
            productArticleInfo.setProductImageList(List.of(productArticleMapper.map(productMainImageMap.get(productArticle))));
            searchResult.add(productArticleInfo);
        });
        return searchResult;
    }

    /**
     * 상품 리스트 조회
     * @param searchInfo
     * @return
     */
    public List<ProductArticleDto> getProductArticles(ProductArticleDto.SearchInfo searchInfo) {
        return null;
    }

    /**
     * 상품 게시글 조회
     * @param productId
     * @return
     */
    public ProductArticleDto getProductArticle(long productId) {
        ProductArticle product = Optional.ofNullable(productArticleRepository.findWithImageAndReviewById(productId))
                .orElseThrow(() -> { throw new ProductNotFoundException("상품을 찾을 수 없습니다."); });

        ProductArticleDto productResult = productArticleMapper.map(product);
        return productResult;
    }

    /**
     * 상품등록
     * @param registerInfo
     * @param userId
     * @return
     */
    @Transactional
    public long registerProductArticle(RegisterProductDto registerInfo, long userId) {
        Category category = categoryRepository.findById(registerInfo.getCategoryId())
                .orElseThrow(() -> new InvalidCategoryIdException(String.format("invalid category id %d", registerInfo.getCategoryId())));
        ProductArticle product = ProductArticle.createProductArticle(registerInfo, category, userId);
        product.addProductImages(registerInfo.getFileIdList());
        productArticleRepository.save(product);
        return product.getId();
    }

    /**
     * 상품 게시글 수정
     * @param changeInfo
     * @param articleId
     * @param userId
     */
    @Transactional
    public void updateProductArticle(ProductArticleDto.ChangeInfo changeInfo, Integer articleId, Integer userId) {
//        ProductArticleDto.Info article = productArticleDao.findOneBy(articleId);
//        if(!article.getUser().getId().equals(userId)){
//            throw new AuthorizationException("게시글 작성자만 글을 수정할 수 있습니다.");
//        }
//
//        productArticleDao.updateBy(changeInfo, articleId);
//        productImageDao.deleteBy(articleId);
//        List<ProductImageDto> productImages = fileUtils.parseImageInfo(articleId, changeInfo.getFiles());
//        productImageDao.createList(productImages);
    }

    /**
     * 상품 게시글 삭제
     * @param articleId
     * @param userId
     */
    @Transactional
    public void deleteProductArticle(Integer articleId, Integer userId) {
//        ProductArticleDto.Info article = productArticleDao.findOneBy(articleId);
//        if(!article.getUser().getId().equals(userId)){
//            throw new AuthorizationException("게시글 작성자만 글을 삭제할 수 있습니다.");
//        }
//        productArticleDao.deleteBy(articleId);
    }

}