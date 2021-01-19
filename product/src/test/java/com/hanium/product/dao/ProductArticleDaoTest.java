package com.hanium.product.dao;

import com.hanium.product.dto.ProductArticleDto;
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

    @Test
    public void updateByTest() {
        ProductArticleDto.ChangeInfo changeInfo = ProductArticleDto.ChangeInfo.builder()
                .title("수정 제목")
                .content("수정 내용")
                .price(100000)
                .categoryId(1)
                .status(((byte) 0))
                .build();
        Assertions.assertThat(dao.updateBy(changeInfo, 15)).isEqualTo(1);
    }
}
