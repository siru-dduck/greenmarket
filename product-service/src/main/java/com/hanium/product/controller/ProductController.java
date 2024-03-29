package com.hanium.product.controller;

import com.hanium.product.controller.validator.ProductListRequestValidator;
import com.hanium.product.domain.user.AuthUserDetail;
import com.hanium.product.dto.*;
import com.hanium.product.dto.mapper.ProductMapper;
import com.hanium.product.dto.request.ProductListRequest;
import com.hanium.product.dto.request.RegisterProductRequest;
import com.hanium.product.dto.request.SearchRequest;
import com.hanium.product.dto.request.UpdateProductRequest;
import com.hanium.product.dto.response.ProductListResponse;
import com.hanium.product.dto.response.ProductResponse;
import com.hanium.product.dto.response.RegisterProductResponse;
import com.hanium.product.service.ProductInterestService;
import com.hanium.product.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author siru
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductInterestService productInterestService;
    private final ProductMapper productMapper;
    private final ProductListRequestValidator productListRequestValidator;

    /**
     * 상품 리스트 검색은 elasticsearch 기반의 별도의 검색서비스로 분리예정
     */
    @ApiOperation(value = "상품검색", notes = "상품검색 api")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok status with search result")
    })
    @Deprecated
    @GetMapping("/search")
    public ResponseEntity<ProductListResponse> searchProducts(@Valid SearchRequest searchRequest) {
        SearchInfoDto searchInfo = productMapper.map(searchRequest);
        List<ProductArticleDto>  searchResult = productService.searchProducts(searchInfo);

        return getProductListResponseResponseEntity(searchResult);
    }

    private ResponseEntity<ProductListResponse> getProductListResponseResponseEntity(List<ProductArticleDto> searchResult) {
        List<ProductResponse> productResponseList = new ArrayList<>();
        searchResult.forEach(productInfo -> {
            ProductResponse productResponse = productMapper.map(productInfo);
            productResponse.setImageFileIdList(productInfo.getProductImageList().stream()
                    .map(ProductImageDto::getFileId)
                    .collect(Collectors.toList()));
            productResponseList.add(productResponse);
        });

        return ResponseEntity.ok(ProductListResponse.createResponse(productResponseList));
    }

    @InitBinder("productListRequest")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(productListRequestValidator);
    }

    @ApiOperation(value = "상품 리스트 조회", notes = "등록자 아이디 및 상품아이디 리스트로 상품 리스트 조회 api")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok status with product list result")
    })
    @GetMapping("/products")
    public ResponseEntity<ProductListResponse> getProductList(
            @Valid ProductListRequest productListRequest) {
        FindProductListDto findProductListInfo = productMapper.map(productListRequest);
        List<ProductArticleDto> findProductResult = productService.getProducts(findProductListInfo);

        return getProductListResponseResponseEntity(findProductResult);
    }

    @ApiOperation(value = "상품등록", notes = "상품검색 api")
    @ApiResponses({
            @ApiResponse(code = 201, message = "created status with product id, url")
    })
    @PostMapping("/products")
    public ResponseEntity<RegisterProductResponse> postProduct(
            @RequestBody @Valid RegisterProductRequest registerRequest,
            AuthUserDetail userDetail) {
        RegisterProductDto registerInfo = productMapper.map(registerRequest);
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

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable long productId,
                               AuthUserDetail userDetail) {
        ProductArticleDto findProduct = productService.getProduct(productId);
        ProductResponse productResponse = productMapper.map(findProduct);
        productResponse.setImageFileIdList(findProduct.getProductImageList().stream()
                .map(ProductImageDto::getFileId)
                .collect(Collectors.toList()));

        // 사용자 인증이 된 경우 게시글에 좋아요를 눌렀는지 여부를 같이 응답에 담는다.
        if (userDetail != null) {
            productResponse.setIsCheckInterest(productInterestService.isCheckInterest(productId, userDetail.getUserId()));
        }
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long productId,
                                                                AuthUserDetail userDetail) {
        productService.deleteProduct(productId, userDetail.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable long productId,
                                              AuthUserDetail userDetail,
                                              @RequestBody UpdateProductRequest updateRequest) throws Exception {
        UpdateProductDto updateInfo = productMapper.map(updateRequest);
        productService.updateProduct(updateInfo, productId, userDetail.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping( "/products/{productId}/addInterest")
    public ResponseEntity<Void> addProductInterest(@PathVariable long productId,
                                                                       AuthUserDetail userDetail) {
        productInterestService.addInterest(productId, userDetail.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping( "/products/{productId}/removeInterest")
    public ResponseEntity<Void> removeProductInterest(
            @PathVariable long productId,
            AuthUserDetail userDetail) {
        productInterestService.removeInterest(productId, userDetail.getUserId());
        return ResponseEntity.ok().build();
    }
}
