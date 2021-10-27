package com.hanium.product.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanium.product.domain.product.Category;
import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.dto.RegisterProductDto;
import com.hanium.product.dto.request.SearchRequest;
import com.hanium.product.repository.CategoryRepository;
import com.hanium.product.repository.ProductArticleImageRepository;
import com.hanium.product.repository.ProductArticleRepository;
import com.hanium.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductArticleRepository productArticleRepository;

    @Autowired
    private ProductArticleImageRepository productArticleImageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private List<ProductArticle> productArticleList = new ArrayList<>();

    @BeforeEach
    void init() {
        Category category = categoryRepository.findById(1L)
                .orElseThrow();
        ProductArticle productArticle1 = ProductArticle.createProductArticle(RegisterProductDto.builder()
                .title("쿠쿠압력밥솥3인용")
                .content("직거래 갤러리아팰리스/잠실역/새내역 문의주세요~")
                .address1("서울특별시")
                .address2("송파구")
                .price(180000)
                .build(), category);
        productArticle1.addProductImage(1);
        productArticle1.addProductImage(2);

        ProductArticle productArticle2 = ProductArticle.createProductArticle(RegisterProductDto.builder()
                .title("쿠쿠압력밥솥3인용")
                .content("직거래 갤러리아팰리스/잠실역/새내역 문의주세요~")
                .address1("서울특별시")
                .address2("송파구")
                .price(180000)
                .build(), category);
        productArticle2.addProductImage(1);
        productArticle2.addProductImage(2);

        productArticleRepository.save(productArticle1);
        productArticleRepository.save(productArticle2);

        productArticleList.add(productArticle1);
        productArticleList.add(productArticle2);
    }

    @Test
    public void 검색테스트() throws Exception {
        // given
        SearchRequest searchRequest = SearchRequest.builder()
                .keyword("쿠쿠")
                .filter("address.address1~서울특별시,address.address2[송파")
                .build();

        // when
        ResultActions result = requestSearch(searchRequest);

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("result").isNotEmpty())
                .andExpect(jsonPath("result").isArray())
                .andExpect(jsonPath("length").value(2))
                .andExpect(jsonPath("lastProductId")
                        .value(Objects.requireNonNull(CollectionUtils.firstElement(productArticleList)).getId()));
    }

    /**
     * 상품 검색 요청
     * @param request
     * @return
     * @throws Exception
     */
    public ResultActions requestSearch(SearchRequest request) throws Exception {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        Map<String, String> maps = new ObjectMapper().convertValue(request, new TypeReference<Map<String, String>>() {});
        queryParams.setAll(maps);

        return mvc.perform(get("/search")
                .queryParams(queryParams))
                .andDo(print());
    }

}
