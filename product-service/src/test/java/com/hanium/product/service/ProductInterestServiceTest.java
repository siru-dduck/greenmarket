package com.hanium.product.service;

import com.hanium.product.domain.product.Category;
import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.ProductInterest;
import com.hanium.product.dto.RegisterProductDto;
import com.hanium.product.repository.CategoryRepository;
import com.hanium.product.repository.ProductArticleRepository;
import com.hanium.product.repository.ProductInterestRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class ProductInterestServiceTest {

    @Autowired
    private ProductInterestService productInterestService;

    @Autowired
    private ProductArticleRepository productArticleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductInterestRepository productInterestRepository;

    private long setUpUserId;
    private long setUpProductId;

    private long createProduct() {
        Category category = categoryRepository.findById(1)
                .orElseThrow();
        long userId = 1;
        RegisterProductDto registerInfo = RegisterProductDto.builder()
                .title("에어팟 프로 팔아요")
                .content("에어팟 프로 팔아요~~~ 애플케어 적용")
                .address1("서울특별시")
                .address2("강서구")
                .categoryId(category.getId())
                .price(180000)
                .fileIdList(List.of(1L, 2L, 3L))
                .build();


        return productService.registerProductArticle(registerInfo, userId);
    }

    @BeforeEach
    void setUp() {
        Category category = categoryRepository.findById(1)
                .orElseThrow();
        ProductArticle product = ProductArticle.createProductArticle(RegisterProductDto.builder()
                .title("쿠쿠압력밥솥3인용")
                .content("직거래 갤러리아팰리스/잠실역/새내역 문의주세요~")
                .address1("서울특별시")
                .address2("송파구")
                .price(180000)
                .build(), category, 1);
        productArticleRepository.save(product);
        this.setUpProductId = product.getId();

        long userId = 1;
        this.setUpUserId = 1;

        ProductInterest productInterest = ProductInterest.createProductInterest(product, userId);
        productInterestRepository.save(productInterest);
    }

    @Test
    public void 상품관심_체크여부_테스트() throws Exception {
        // given
        long userId = this.setUpUserId;
        long productId = this.setUpProductId;

        // when
        boolean result = productInterestService.isCheckInterest(productId, userId);

        // then
        assertThat(result).isEqualTo(true);
    }
}
