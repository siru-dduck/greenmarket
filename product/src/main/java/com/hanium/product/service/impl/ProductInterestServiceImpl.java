package com.hanium.product.service.impl;

import com.hanium.product.dao.IProductArticleDao;
import com.hanium.product.dao.IProductInterestDao;
import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.exception.AlreadyExistResourceException;
import com.hanium.product.exception.AuthorizationException;
import com.hanium.product.exception.NotFoundResourceException;
import com.hanium.product.service.ProductInterestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProductInterestServiceImpl implements ProductInterestService {

    private final IProductArticleDao productArticleDao;
    private final IProductInterestDao productInterestDao;

    @Override
    public boolean isCheckedInterest(Integer articleId, Integer userId) {
        return productInterestDao.check(articleId, userId) != 0;
    }

    @Override
    @Transactional
    public void addInterest(Integer articleId, Integer userId) {
        ProductArticleDto.Info article = productArticleDao.findBy(articleId);
        if(article.getUser().getId().equals(userId)) {
            throw new AuthorizationException("상품 작성자는 해당 관심상품을 추가할 수 없습니다.");
        }
        if( productInterestDao.check(articleId, userId) != 0) {
            throw new AlreadyExistResourceException("이미 등록한 관심상품이 있습니다.");
        }
        productInterestDao.create(articleId, userId);
        productArticleDao.addInterestCount(articleId);
    }

    @Override
    @Transactional
    public void removeInterest(Integer articleId, Integer userId) {
        if(productInterestDao.check(articleId, userId) == 0) {
            throw new NotFoundResourceException("제거할 관심상품이 없습니다.");
        }
        productInterestDao.delete(articleId, userId);
        productArticleDao.subtractInterestCount(articleId);
    }
}
