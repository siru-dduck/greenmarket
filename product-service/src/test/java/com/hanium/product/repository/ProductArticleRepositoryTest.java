package com.hanium.product.repository;

import com.hanium.product.domain.product.Category;
import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.ProductArticleStatus;
import com.hanium.product.dto.RegisterProductDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@DataJpaTest
class ProductArticleRepositoryTest {

    @Autowired
    private ProductArticleRepository productArticleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void 상품등록_테스트() throws Exception {
        // given
        Category category = categoryRepository.findById(1L)
                .orElseThrow();
        ProductArticle productArticle = ProductArticle.createProductArticle(RegisterProductDto.builder()
                .title("에어팟 프로 팔아요")
                .content("에어팟 프로 팔아요~~~ 애플케어 적용")
                .address1("서울특별시")
                .address2("강서구")
                .price(180000)
                .build(), category);


        // when
        productArticleRepository.save(productArticle);
        ProductArticle findProductArticle = productArticleRepository.findById(productArticle.getId())
                .orElseThrow();

        // then
        assertThat(productArticle).isSameAs(findProductArticle);
        assertThat(findProductArticle.getId()).isEqualTo(productArticle.getId());
        assertThat(findProductArticle.getTitle()).isEqualTo(productArticle.getTitle());
        assertThat(findProductArticle.getContent()).isEqualTo(productArticle.getContent());
        assertThat(findProductArticle.getCreateDate()).isEqualTo(productArticle.getCreateDate());
        assertThat(findProductArticle.getPrice()).isEqualTo(productArticle.getPrice());
        assertThat(findProductArticle.getStatus()).isEqualTo(ProductArticleStatus.SALE);
    }

}