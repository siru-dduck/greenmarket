package com.hanium.product.service;


import com.hanium.product.repository.ProductInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author siru
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductInterestService {

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

    }

    public void removeInterest(long productId, long userId) {

    }
}
