package com.hanium.product.service;

import com.hanium.product.domain.product.Category;
import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.ProductStatus;
import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.RegisterProductDto;
import com.hanium.product.exception.BusinessException;
import com.hanium.product.exception.ProductNotFoundException;
import com.hanium.product.repository.CategoryRepository;
import com.hanium.product.repository.ProductArticleImageRepository;
import com.hanium.product.repository.ProductArticleRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductArticleRepository productArticleRepository;

    @Autowired
    private ProductArticleImageRepository productArticleImageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

    @Test
    public void 상품등록_성공테스트() throws Exception {
        // given
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

        // when
        long saveProductId = productService.registerProductArticle(registerInfo, userId);

        // then
        ProductArticle findProduct = productArticleRepository.findById(saveProductId).orElseThrow();
        assertThat(findProduct.getTitle()).isEqualTo("에어팟 프로 팔아요");
        assertThat(findProduct.getContent()).isEqualTo("에어팟 프로 팔아요~~~ 애플케어 적용");
        assertThat(findProduct.getAddress().getAddress1()).isEqualTo("서울특별시");
        assertThat(findProduct.getAddress().getAddress2()).isEqualTo("강서구");
        assertThat(findProduct.getPrice()).isEqualTo(180000);
        assertThat(findProduct.getCategory().getName()).isEqualTo("디지털/가전");
        assertThat(findProduct.getUserId()).isEqualTo(userId);
        assertThat(findProduct.getProductImageList()).asList().size().isEqualTo(3);
        assertThat(findProduct.getMainProductImage()).isEqualTo(CollectionUtils.firstElement(findProduct.getProductImageList()));
    }

    @Test
    public void 상품단건조회_성공테스트() throws Exception {
        // given
        long productId = createProduct();

        // when
        ProductArticleDto productResult = productService.getProduct(productId);

        // then
        assertThat(productResult.getId()).isEqualTo(productId);
    }

    @Test
    public void 존재하지않는_상품_단건조회_실패테스트() throws Exception {
        // given
        long productId = -999; // invalid product id

        // when
        Throwable thrown = catchThrowable(() -> {
            productService.getProduct(productId);
        });

        // then
        assertThat(thrown).isInstanceOf(ProductNotFoundException.class)
                .isInstanceOf(BusinessException.class);
    }
    
    @Test
    public void 상품삭제_테스트() throws Exception {
        // given
        long productId = createProduct();

        // when
        productService.deleteProduct(productId, 1);

        // then
        ProductArticle findProduct = productArticleRepository.findById(productId)
                .orElseThrow();

        assertThat(findProduct.getStatus()).isEqualTo(ProductStatus.DELETE);
    }
}
