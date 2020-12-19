package com.hanium.product.service;

import com.hanium.product.dao.IProductArticleDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
public class ProductInterestServiceTest {

    @Autowired
    private ProductInterestService productInterestService;
    @Autowired
    private IProductArticleDao productArticleDao;
    @Test
    void addInterestCountTest(){
        int beforeInterestCount = productArticleDao.findBy(11).getInterestCount();
        System.out.println("✅ 업데이트여부 : " + (productInterestService.addInterestCount(11, 4) == 1));
        System.out.println("✅ 업데이트여부 : " + (productInterestService.addInterestCount(11, 3) == 1));
        int afterInterestCount = productArticleDao.findBy(11).getInterestCount();
        Assertions.assertThat(afterInterestCount - beforeInterestCount).isEqualTo(2);
    }
}
