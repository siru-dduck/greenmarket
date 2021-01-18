package com.hanium.product.service;

public interface ProductInterestService {
    boolean isCheckedInterest(Integer articleId, Integer userId);

    void addInterest(Integer articleId, Integer userId);

    void removeInterest(Integer articleId, Integer userId);
}
