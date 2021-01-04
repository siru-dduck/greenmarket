package com.hanium.product.service.impl;

import com.hanium.product.dao.IProductArticleDao;
import com.hanium.product.dao.IProductInterestDao;
import com.hanium.product.service.ProductInterestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class ProductInterestServiceImpl implements ProductInterestService {

    private final IProductArticleDao productArticleDao;
    private final IProductInterestDao productInterestDao;

    public ProductInterestServiceImpl(IProductArticleDao productArticleDao, IProductInterestDao productInterestDao) {
        this.productArticleDao = productArticleDao;
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
    @Transactional
    public void addInterestCount(Integer articleId, Integer userId) {
        productInterestDao.create(articleId, userId);
        productArticleDao.addInterestCount(articleId);
    }

    @Override
    @Transactional
    public void subtractInterestCount(Integer articleId, Integer userId) {
        if(productInterestDao.delete(articleId, userId) == 0) {
            throw new RuntimeException("Not Exist Interest For Delete");
        }
        productArticleDao.subtractInterestCount(articleId);
    }
}
