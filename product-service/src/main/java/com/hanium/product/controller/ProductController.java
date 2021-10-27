package com.hanium.product.controller;

import com.hanium.product.domain.user.AuthUserDetail;
import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.RegisterProductDto;
import com.hanium.product.dto.SearchInfoDto;
import com.hanium.product.dto.UserDto;
import com.hanium.product.dto.mapper.ProductArticleMapper;
import com.hanium.product.dto.request.RegisterProductRequest;
import com.hanium.product.dto.request.SearchRequest;
import com.hanium.product.dto.response.ProductListResponse;
import com.hanium.product.dto.response.ProductResponse;
import com.hanium.product.dto.response.RegisterProductResponse;
import com.hanium.product.service.ChatService;
import com.hanium.product.service.ProductInterestService;
import com.hanium.product.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

/**
 * @author siru
 */
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ChatService chatService;
    private final ProductInterestService productInterestService;
    private final ProductArticleMapper productArticleMapper;

    /**
     * 상품 리스트 검색은 elasticsearch 기반의 별도의 검색서비스로 분리예정
     */
    @ApiOperation(value = "상품검색", notes = "상품검색 api")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok status with search result")
    })
    @GetMapping("/search")
    public ResponseEntity<ProductListResponse> searchProducts(@Valid SearchRequest searchRequest) {
        SearchInfoDto searchInfo = productArticleMapper.map(searchRequest);
        List<ProductArticleDto>  searchResult = productService.searchProductArticles(searchInfo);
        List<ProductResponse> productList = new ArrayList<>();
        searchResult.forEach(productInfo -> {
            productList.add(productArticleMapper.map(productInfo));
        });

        ProductListResponse response = ProductListResponse.builder()
                .data(productList)
                .lastProductId(productList.size() > 0 ? Objects.requireNonNull(CollectionUtils.lastElement(productList)).getId() : null)
                .count(productList.size())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    public Map<String, Object> getProductList(
            @Valid ProductArticleDto.SearchInfo searchInfo) {
        Map<String, Object> result = new HashMap<>();
        List<ProductArticleDto.Info> articleList = productService.getProductArticles(searchInfo);
        result.put("productArticles", articleList);
        if(articleList.size() > 0) {
            result.put("length", articleList.size());
            result.put("lastArticleId", articleList.get(articleList.size()-1).getId());
        } else {
            result.put("length", 0);
            result.put("lastArticleId", null);
        }
        return result;
    }

    @ApiOperation(value = "상품등록", notes = "상품검색 api")
    @ApiResponses({
            @ApiResponse(code = 201, message = "created status with product id, url")
    })
    @PostMapping("/products")
    public ResponseEntity<RegisterProductResponse> postProduct(
            @RequestBody @Valid RegisterProductRequest registerRequest,
            AuthUserDetail userDetail) {
        RegisterProductDto registerInfo = productArticleMapper.map(registerRequest);
        long productId = productService.registerProductArticle(registerInfo, userDetail.getUserId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri();
        RegisterProductResponse response = RegisterProductResponse.builder()
                .productId(productId)
                .link(location.toString())
                .build();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/products/{id}")
    public Map<String, Object> getProduct(@PathVariable Integer id,
                                          UserDto.Info user) {
        Map<String, Object> result = new HashMap<>();
        result.put("productArticle", productService.getProductArticle(id));

        if (user == null) {
            result.put("chatRoomId", null);
        } else {
            Integer userId = user.getId();
            result.put("chatRoomId", chatService.getChatRoomId(id, userId));
            result.put("isCheckedInterest", productInterestService.isCheckedInterest(id, userId));
        }
        return result;
    }

    @DeleteMapping("/products/{articleId}")
    public ResponseEntity<ProductArticleDto.Info> deleteProduct(@PathVariable Integer articleId,
                                                             UserDto.Info user) {
        productService.deleteProductArticle(articleId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/products/{articleId}")
    public ResponseEntity<ProductArticleDto.Info> updateProduct(@PathVariable Integer articleId,
                                                             UserDto.Info user,
                                                             @Valid ProductArticleDto.ChangeInfo changeInfo) throws Exception {
        productService.updateProductArticle(changeInfo, articleId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping( "/products/{id}/interest")
    public ResponseEntity addProductInterestCount(@PathVariable Integer id,
                                                                       UserDto.Info user) {
        productInterestService.addInterest(id, user.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping( "/products/{id}/interest")
    public ResponseEntity subtractProductInterestCount(
            @PathVariable Integer id,
            UserDto.Info user) {
        productInterestService.removeInterest(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
