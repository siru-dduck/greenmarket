package com.hanium.product.service;

public interface ProductInterestService {
    int checkInterest(Integer articleId, Integer userId);
    int getInterestCountBy(Integer articleId);
    int addInterestCount(Integer articleId, Integer userId);
    int subtractInterestCount(Integer articleId, Integer userId);
}
