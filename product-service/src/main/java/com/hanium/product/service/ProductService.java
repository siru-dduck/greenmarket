package com.hanium.product.service;

import com.hanium.product.domain.product.Category;
import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.ProductImage;
import com.hanium.product.dto.*;
import com.hanium.product.dto.mapper.ProductMapper;
import com.hanium.product.exception.InvalidCategoryIdException;
import com.hanium.product.exception.ProductNotFoundException;
import com.hanium.product.exception.UserAuthorizationException;
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
    private final ProductMapper productMapper;
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
    public List<ProductArticleDto> searchProducts(SearchInfoDto searchInfo) {
        List<ProductArticle> productList = productArticleRepository.findBySearchQuery(searchInfo);
        Map<ProductArticle, ProductImage> productMainImageMap = getProductMainImageMap(productList);

        List<ProductArticleDto> searchResult = new ArrayList<>();
        productList.forEach(productArticle -> {
            ProductArticleDto productArticleInfo = productMapper.map(productArticle);
            productArticleInfo.setProductImageList(List.of(productMapper.map(productMainImageMap.get(productArticle))));
            searchResult.add(productArticleInfo);
        });
        return searchResult;
    }

    /**
     * 상품 리스트 조회
     * @param findProductListInfo
     * @return
     */
    public List<ProductArticleDto> getProducts(FindProductListDto findProductListInfo) {
        List<ProductArticle> productList = productArticleRepository.findListBy(findProductListInfo);
        Map<ProductArticle, ProductImage> productMainImageMap = getProductMainImageMap(productList);

        List<ProductArticleDto> findProductList = new ArrayList<>();
        productList.forEach(productArticle -> {
            ProductArticleDto productArticleInfo = productMapper.map(productArticle);
            productArticleInfo.setProductImageList(List.of(productMapper.map(productMainImageMap.get(productArticle))));
            findProductList.add(productArticleInfo);
        });
        return findProductList;
    }

    /**
     * 상품 메인 이미지 상품별로 검색후 grouping
     * @param productList
     * @return
     */
    private Map<ProductArticle, ProductImage> getProductMainImageMap(List<ProductArticle> productList) {
        return productArticleImageRepository.findMainImageByFileIdIn(
                        productList.stream()
                                .map(ProductArticle::getId)
                                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(ProductImage::getProductArticle, Function.identity()));
    }

    /**
     * 상품 게시글 조회
     * @param productId
     * @return
     */
    public ProductArticleDto getProduct(long productId) {
        ProductArticle product = Optional.ofNullable(productArticleRepository.findWithImageAndReviewById(productId))
                .orElseThrow(() -> { throw new ProductNotFoundException("상품을 찾을 수 없습니다."); });

        ProductArticleDto productResult = productMapper.map(product);
        List<ProductImageDto> productImageList = product.getProductImageList().stream()
                .map(productMapper::map)
                .collect(Collectors.toList());
        productResult.setProductImageList(productImageList);
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
        product.setProductImages(registerInfo.getFileIdList());
        productArticleRepository.save(product);
        return product.getId();
    }

    /**
     * 상품 게시글 수정
     * @param updateInfo
     * @param productId
     * @param userId
     */
    @Transactional
    public void updateProduct(UpdateProductDto updateInfo, long productId, long userId) {
        ProductArticle product = productArticleRepository.findById(productId)
                .orElseThrow(() -> {
                    throw new ProductNotFoundException("상품을 찾을 수 없습니다.");
                });
        Category category = categoryRepository.findById(updateInfo.getCategoryId())
                .orElseThrow(() -> new InvalidCategoryIdException(String.format("invalid category id %d", updateInfo.getCategoryId())));

        // 권한 검사
        if (product.getUserId() != userId) {
            throw new UserAuthorizationException("권한이 없는 사용자 입니다.");
        }

        product.updateProduct(updateInfo, category);
        product.setProductImages(updateInfo.getFileIdList());
    }

    /**
     * 상품 게시글 삭제
     * @param productId
     * @param userId
     */
    @Transactional
    public void deleteProduct(long productId, long userId) {
        ProductArticle product = productArticleRepository.findById(productId)
                .orElseThrow(() -> {
                    throw new ProductNotFoundException("상품을 찾을 수 없습니다.");
                });

        // 권한 검사
        if (product.getUserId() != userId) {
            throw new UserAuthorizationException("권한이 없는 사용자 입니다.");
        }

        product.deleteProduct();
    }

}