package siru.frontservice.repository.product;

import feign.Headers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import siru.frontservice.domain.product.read.Product;

@ReactiveFeignClient(name = "product-service", url = "http://localhost:8110")
@Headers({ "Accept: application/json" })
public interface ProductRepository {

    @GetMapping("/products/{productId}")
    Mono<Product> getProduct(@PathVariable long productId);
}
