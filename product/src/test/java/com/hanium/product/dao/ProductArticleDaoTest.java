package com.hanium.product.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class ProductArticleDaoTest {
    @Autowired
    IProductArticleDao dao;

    @Test
    public void deleteByTest(){
        Assertions.assertThat(dao.deleteBy(13)).isEqualTo(1);
    }
}
