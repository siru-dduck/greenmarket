package com.hanium.product.service.impl;

import com.hanium.product.dao.IProductInterestDao;
import com.hanium.product.service.ProductInterestService;
import org.springframework.stereotype.Service;

@Service
public class ProductInterestServiceImpl implements ProductInterestService {

    private final IProductInterestDao productInterestDao;

    public ProductInterestServiceImpl(IProductInterestDao productInterestDao) {
        this.productInterestDao = productInterestDao;
    }

    @Override
    public int getInterestCountBy(Integer articleId) {
        return productInterestDao.count(articleId);
    }

    @Override
    public int addInterest(Integer articleId, Integer userId) {
        return productInterestDao.create(articleId, userId);
    }

    @Override
    public int subtractInterest(Integer articleId, Integer userId) {
        return productInterestDao.delete(articleId, userId);
    }
}
