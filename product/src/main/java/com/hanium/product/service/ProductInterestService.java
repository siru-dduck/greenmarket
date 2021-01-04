package com.hanium.product.service;

public interface ProductInterestService {
    int checkInterest(Integer articleId, Integer userId);
    int getInterestCountBy(Integer articleId);
    void addInterestCount(Integer articleId, Integer userId);
    void subtractInterestCount(Integer articleId, Integer userId);
}
