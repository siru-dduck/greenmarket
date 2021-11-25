package siru.frontservice.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import siru.frontservice.domain.AuthUserDetail;
import siru.frontservice.domain.product.read.Product;
import siru.frontservice.service.ProductQueryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AggregateController {

    private final ProductQueryService productQueryService;

    @ApiOperation(value = "상품 상세정보 조회", notes = "상품 상세정보 조회 api")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok status with product info")
    })
    @GetMapping("/products/{productId}")
    public Mono<ResponseEntity<Product>> getProduct(@PathVariable long productId) {
        return productQueryService.getProduct(productId, AuthUserDetail.builder().build())
                .map(ResponseEntity::ok);
    }

}
