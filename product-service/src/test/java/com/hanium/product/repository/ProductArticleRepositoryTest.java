package com.hanium.product.repository;

import com.hanium.product.domain.product.Category;
import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.ProductArticleStatus;
import com.hanium.product.domain.product.QProductArticle;
import com.hanium.product.dto.RegisterProductDto;
import com.hanium.product.dto.SearchInfoDto;
import com.hanium.product.repository.querydsl.ProductArticlePredicatesBuilder;
import com.hanium.product.repository.querydsl.SearchOperation;
import com.querydsl.jpa.impl.JPAQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        productArticle1.addProductImage(1);
        productArticle1.addProductImage(2);
        productArticle1.addProductImage(3);
        productArticle1.addProductImage(4);
        productArticle1.addProductImage(5);
        productArticle1.addProductImage(6);
        productArticle1.addProductImage(7);
        productArticle1.addProductImage(8);
        productArticle1.addProductImage(9);
        productArticle1.addProductImage(10);
        productArticle1.addProductImage(11);
        productArticle1.addProductImage(12);
        productArticle1.addProductImage(13);
        productArticle1.addProductImage(14);
        productArticle1.addProductImage(15);
        productArticle1.addProductImage(16);
        productArticle1.addProductImage(17);
        productArticle1.addProductImage(18);
        productArticle1.addProductImage(19);
        productArticle1.addProductImage(20);
        productArticle1.addProductImage(21);
        productArticle1.addProductImage(22);
        productArticle1.addProductImage(23);
        productArticle1.addProductImage(24);
        productArticle1.addProductImage(25);

        productArticleRepository.save(productArticle1);
        productArticleRepository.save(productArticle2);
        productArticleRepository.save(productArticle3);

        SearchInfoDto searchInfo = SearchInfoDto.builder()
                .filter("address.address1~서울특별시,address.address2[강,price>z")
                .size(20)
                .offset(0)
                .build();

        // when
        List<ProductArticle> productArticleList = productArticleRepository.findBySearchQuery(searchInfo);

        // then
        assertThat(productArticleList).asList().size().isEqualTo(3);
        assertThat(productArticleList).asList().contains(productArticle1, productArticle2, productArticle3);
    }

    @Test
    public void 상품검색_테스트2() throws Exception {
        // given
        Category category = categoryRepository.findById(1L)
                .orElseThrow();
        ProductArticle productArticle1 = ProductArticle.createProductArticle(RegisterProductDto.builder()
                .title("쿠쿠압력밥솥3인용")
                .content("직거래 갤러리아팰리스/잠실역/새내역 문의주세요~")
                .address1("서울특별시")
                .address2("송파구")
                .price(180000)
                .build(), category);

        ProductArticle productArticle2 = ProductArticle.createProductArticle(RegisterProductDto.builder()
                .title("스팸 8K호")
                .content("스팸 8k호 판매합니다 24년 ? 정도까지 인듯합니다 스팸 안먹어서 올립니다")
                .address1("서울특별시")
                .address2("송파구")
                .price(10000)
                .build(), category);

        ProductArticle productArticle3 = ProductArticle.createProductArticle(RegisterProductDto.builder()
                .title("쿠쿠압력밥솥1인용")
                .content("따근한 밥 먹을땐 역시 쿠쿠~")
                .address1("서울특별시")
                .address2("강서구")
                .price(90000)
                .build(), category);

        productArticleRepository.save(productArticle1);
        productArticleRepository.save(productArticle2);
        productArticleRepository.save(productArticle3);

        // when
        ProductArticlePredicatesBuilder builder = new ProductArticlePredicatesBuilder()
                .with("쿠쿠")
                .with("address.address1", SearchOperation.LIKE, "서울특별시")
                .with("address.address2", SearchOperation.LIKE, "송파구");
//        Iterable<ProductArticle> productArticleList = productArticleRepository.findAll(builder.build());

        // then
//        assertThat(productArticleList).asList().size().isEqualTo(1);
//        assertThat(productArticleList).asList().contains(productArticle1);
    }

    @Test
    public void 패턴테스트() throws Exception {
        // given
        String filter = "address.address1~서울특별시,address.address2~강,price>z,title[]에어팟";

        // when then
        Pattern pattern = Pattern.compile("([\\w|.]+?)(=|~|<|>|\\[|]|\\[])([ㄱ-ㅎ|ㅏ-ㅣ가-힣\\w]+?),");
        Matcher matcher = pattern.matcher(filter + ",");
        while (matcher.find()) {
            System.out.println(matcher.group(1)+ " " + SearchOperation.getSimpleOperation(matcher.group(2)) + " " + matcher.group(3));
        }

    }
}