package com.hanium.product.repository;

import com.hanium.product.domain.product.Category;
import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.ProductArticleStatus;
import com.hanium.product.dto.RegisterProductDto;
import com.hanium.product.repository.querydsl.ProductArticlePredicatesBuilder;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

    @Test
    public void 상품검색_테스트() throws Exception {
        // given
        Category category = categoryRepository.findById(1L)
                .orElseThrow();
        ProductArticle productArticle1 = ProductArticle.createProductArticle(RegisterProductDto.builder()
                .title("에어팟 프로 팔아요")
                .content("에어팟 프로 팔아요~~~ 애플케어 적용")
                .address1("서울특별시")
                .address2("강서구")
                .price(180000)
                .build(), category);

        ProductArticle productArticle2 = ProductArticle.createProductArticle(RegisterProductDto.builder()
                .title("에어팟 프로 팔아요")
                .content("에어팟 프로 팔아요~~~ 애플케어 적용")
                .address1("서울특별시")
                .address2("강남구")
                .price(200000)
                .build(), category);

        ProductArticle productArticle3 = ProductArticle.createProductArticle(RegisterProductDto.builder()
                .title("애플 에어팟 프로 팔아요")
                .content("에어팟 프로 팔아요~~~ 애플케어 적용")
                .address1("서울특별시")
                .address2("강서구")
                .price(135000)
                .build(), category);

        productArticleRepository.save(productArticle1);
        productArticleRepository.save(productArticle2);
        productArticleRepository.save(productArticle3);

        // when
        ProductArticlePredicatesBuilder builder = new ProductArticlePredicatesBuilder()
                .with("address.address1", ":", "서울특별시")
                .with("address.address2", ":", "강남구");

        Iterable<ProductArticle> productArticleList = productArticleRepository.findAll(builder.build());

        // then
        assertThat(productArticleList).asList().contains(productArticle2);
    }
}