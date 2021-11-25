package siru.frontservice.service;

import feign.Feign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import siru.frontservice.domain.AuthUserDetail;
import siru.frontservice.domain.product.read.Product;
import siru.frontservice.repository.product.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository productRepository;

    /**
     * 상품상세정보 조회
     * @param productId
     * @param authUserDetail
     * @return
     */
    public Mono<Product> getProduct(long productId, AuthUserDetail authUserDetail) {
        return productRepository.getProduct(productId);
    }

}
