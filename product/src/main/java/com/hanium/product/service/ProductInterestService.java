package com.hanium.product.service;

public interface ProductInterestService {
    int checkInterest(Integer articleId, Integer userId);
    int getInterestCountBy(Integer articleId);
    int addInterest(Integer articleId, Integer userId);
    int subtractInterest(Integer articleId, Integer userId);
}
