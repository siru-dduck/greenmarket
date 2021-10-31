package com.hanium.product.service;


import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.ProductInterest;
import com.hanium.product.exception.ProductNotFoundException;
import com.hanium.product.exception.UserAuthorizationException;
import com.hanium.product.repository.ProductArticleRepository;
import com.hanium.product.repository.ProductInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author siru
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductInterestService {

    private final ProductArticleRepository productArticleRepository;
    private final ProductInterestRepository productInterestRepository;


    /*************************************************
     * 관심상품 관련 비즈니스 로직
     *************************************************/

    /**
     * 상품 관심 등록 여부 체크
     * @param productId
     * @param userId
     * @return
     */
    public boolean isCheckInterest(long productId, long userId) {
        return productInterestRepository.existsByProductArticleIdAndUserId(productId, userId);
    }

    public void addInterest(long productId, long userId) {
        ProductArticle product = Optional.ofNullable(productArticleRepository.findWithImageAndReviewById(productId))
                .orElseThrow(() -> { throw new ProductNotFoundException("상품을 찾을 수 없습니다."); });

        // 권한 검사
        if (product.getUserId() != userId) {
            throw new UserAuthorizationException("권한이 없는 사용자 입니다.");
        }

        product.addInterestCount();
        productInterestRepository.save(ProductInterest.createProductInterest(product, userId));
    }

    public void removeInterest(long productId, long userId) {
        ProductArticle product = Optional.ofNullable(productArticleRepository.findWithImageAndReviewById(productId))
                .orElseThrow(() -> { throw new ProductNotFoundException("상품을 찾을 수 없습니다."); });

        // 권한 검사
        if (product.getUserId() != userId) {
            throw new UserAuthorizationException("권한이 없는 사용자 입니다.");
        }

        product.removeInterestCount();
        productInterestRepository.deleteByProductArticleEqualsAndUserId(product, userId);
    }
}
