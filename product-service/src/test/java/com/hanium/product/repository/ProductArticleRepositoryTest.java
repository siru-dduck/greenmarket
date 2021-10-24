package com.hanium.product.repository;

import com.hanium.product.domain.product.ProductArticle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
class ProductArticleRepositoryTest {

    @Autowired
    private ProductArticleRepository productArticleRepository;

    @Test
    @Transactional
    public void 상품등록_테스트() throws Exception {
        // given
        ProductArticle productArticle = ProductArticle.builder()
                .title("에어팟 프로 팔아요")
                .content("에어팟 프로 팔아요~~~ 애플케어 적용")
                .writeDate(LocalDateTime.now())
                .price(180000)
                .build();

        // when
        Long saveId = productArticleRepository.save(productArticle);
        ProductArticle findProductArticle = productArticleRepository.findById(saveId);

        // then
        assertSame(productArticle, findProductArticle);
        assertEquals(findProductArticle.getId(), productArticle.getId());
        assertEquals(findProductArticle.getTitle(), productArticle.getTitle());
        assertEquals(findProductArticle.getContent(), productArticle.getContent());
        assertEquals(findProductArticle.getWriteDate(), productArticle.getWriteDate());
        assertEquals(findProductArticle.getPrice(), productArticle.getPrice());
    }

}