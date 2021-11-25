package siru.frontservice.config.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivefeign.spring.config.EnableReactiveFeignClients;
import reactivefeign.webclient.WebReactiveFeign;
import siru.frontservice.repository.product.ProductRepository;

@Configuration
public class FeignConfig {

//    @Bean
//    public ProductRepository productRepository() {
//        return WebReactiveFeign
//                .<ProductRepository>builder()
//                .target(ProductRepository.class, "http://localhost:8110");
//    }
}
