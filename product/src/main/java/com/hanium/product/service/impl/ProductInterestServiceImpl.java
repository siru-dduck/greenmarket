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
    public int checkInterest(Integer articleId, Integer userId) {
        return productInterestDao.check(articleId, userId);
    }

    @Override
    public int getInterestCountBy(Integer articleId) {
        return productInterestDao.count(articleId);
    }

    @Override
    public int addInterest(Integer articleId, Integer userId) {
        try{
            return productInterestDao.create(articleId, userId);
        } catch(Exception e) {
            return 0;
        }
    }

    @Override
    public int subtractInterest(Integer articleId, Integer userId) {
        try{
            return productInterestDao.delete(articleId, userId);
        } catch(Exception e) {
            return 0;
        }
    }
}
