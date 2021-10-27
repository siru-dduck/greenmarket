package com.hanium.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanium.product.config.security.JwtProvider;
import com.hanium.product.domain.product.Category;
import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.user.AuthUserDetail;
import com.hanium.product.dto.RegisterProductDto;
import com.hanium.product.dto.request.RegisterProductRequest;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private JwtProvider jwtProvider;

    private final List<ProductArticle> productArticleList = new ArrayList<>();

    @BeforeEach
    void init() {
        Category category = categoryRepository.findById(1)
                .orElseThrow();
        ProductArticle productArticle1 = ProductArticle.createProductArticle(RegisterProductDto.builder()
                .title("쿠쿠압력밥솥3인용")
                .content("직거래 갤러리아팰리스/잠실역/새내역 문의주세요~")
                .address1("서울특별시")
                .address2("송파구")
                .price(180000)
                .build(), category, 1);
        productArticle1.addProductImage(1);
        productArticle1.addProductImage(2);

        ProductArticle productArticle2 = ProductArticle.createProductArticle(RegisterProductDto.builder()
                .title("쿠쿠압력밥솥3인용")
                .content("직거래 갤러리아팰리스/잠실역/새내역 문의주세요~")
                .address1("서울특별시")
                .address2("송파구")
                .price(180000)
                .build(), category, 2);
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
                .andExpect(jsonPath("data").isNotEmpty())
                .andExpect(jsonPath("data").isArray())
                .andExpect(jsonPath("count").value(2))
                .andExpect(jsonPath("lastProductId")
                        .value(Objects.requireNonNull(CollectionUtils.firstElement(productArticleList)).getId()));
    }

    @Test
    public void 상품등록_성공테스트() throws Exception {
        // given
        RegisterProductRequest registerRequest = RegisterProductRequest.builder()
                .title("에어팟 프로 팔아요")
                .content("에어팟 프로 팔아요~~~ 애플케어 적용")
                .address1("서울특별시")
                .address2("강서구")
                .categoryId(1) // 유효하지 않은 카테고리 아이디
                .price(180000)
                .fileIdList(List.of(1L, 2L, 3L))
                .build();

        // when
        ResultActions result = requestRegisterProduct(registerRequest);

        // then
        result
                .andExpect(status().isCreated());
    }

    @Test
    public void 상품등록시_잘못된_카테고리_아이디로_요청_실패테스트() throws Exception {
        // given
        RegisterProductRequest registerRequest = RegisterProductRequest.builder()
                .title("에어팟 프로 팔아요")
                .content("에어팟 프로 팔아요~~~ 애플케어 적용")
                .address1("서울특별시")
                .address2("강서구")
                .categoryId(-1) // 유효하지 않은 카테고리 아이디
                .price(180000)
                .fileIdList(List.of(1L, 2L, 3L))
                .build();

        // when
        ResultActions result = requestRegisterProduct(registerRequest);

        // then
        result
                .andExpect(status().isBadRequest());
    }

    private String createAccessToken() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test@email.com", null, new ArrayList<>());
        AuthUserDetail userDetail = AuthUserDetail.builder()
                .userId(1)
                .email("test@email.com")
                .nickname("test nickname")
                .profileImageId(null)
                .tokenId(UUID.randomUUID().toString())
                .build();
        authentication.setDetails(userDetail);
        return jwtProvider.createAccessToken(authentication);
    }

    private ResultActions requestRegisterProduct(RegisterProductRequest registerRequest) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(createAccessToken());

        return mvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequest))
                        .headers(headers))
                .andDo(print());
    }

    /**
     * 상품 검색 요청
     * @param request
     * @return
     * @throws Exception
     */
    private ResultActions requestSearch(SearchRequest request) throws Exception {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        Map<String, String> maps = new ObjectMapper().convertValue(request, new TypeReference<Map<String, String>>() {});
        queryParams.setAll(maps);

        return mvc.perform(get("/search")
                .queryParams(queryParams))
                .andDo(print());
    }

}
