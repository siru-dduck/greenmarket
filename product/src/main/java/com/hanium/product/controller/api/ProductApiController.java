package com.hanium.product.controller.api;

import com.hanium.product.dto.*;
import com.hanium.product.service.ChatService;
import com.hanium.product.service.JwtService;
import com.hanium.product.service.ProductInterestService;
import com.hanium.product.service.ProductService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/products")
@AllArgsConstructor
public class ProductApiController {

    private final ProductService productService;
    private final JwtService jwtService;
    private final ChatService chatService;
    private final ProductInterestService productInterestService;

    @GetMapping
    public Map<String, Object> getProductList(
            @Valid ProductArticleDto.SearchInfo searchInfo) {
        Map<String, Object> result = new HashMap<>();
        result.put("productArticles", productService.getProductArticles(searchInfo));
        return result;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> postProduct(
            @Valid ProductArticleDto.RegisterInfo registerInfo,
            @CookieValue(name = "x_auth", required = false) String token) {
        Map<String, Object> result = new HashMap<>();

        // TODO AOP 적용 또는 스프링 시큐리티 적용
        // jwt 유효성 검사
        Jws<Claims> claim = jwtService.decodeToken(token);
        if (claim == null) {
            result.put("message", "Not Authenticated");
            return ResponseEntity.status(401).body(result);
        }

        try {
            ProductArticleDto.Info productArticle = ProductArticleDto.Info.builder()
                    .title(registerInfo.getTitle())
                    .content(registerInfo.getContent())
                    .price(registerInfo.getPrice())
                    .user(UserDto.builder().id((Integer) claim.getBody().get("id")).build())
                    .category(CategoryDto.builder().id(registerInfo.getCategoryId()).build())
                    .build();
            Integer articleId = productService.createProductArticle(productArticle, registerInfo.getFiles());
            result.put("articleId", articleId);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(articleId)
                    .toUri();
            return ResponseEntity.created(location).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(path = "/{id}")
    public Map<String, Object> getProduct(@PathVariable Integer id,
                                          @CookieValue(name = "x_auth", required = false) String token) {
        Map<String, Object> result = new HashMap<>();
        result.put("productArticle", productService.getProductArticle(id));
        result.put("productImages", productService.getProductImages(id));

        // TODO AOP 적용 또는 스프링 시큐리티 적용
        // jwt 유효성 검사
        Jws<Claims> claim = jwtService.decodeToken(token);
        if (claim == null) {
            result.put("chatRoomId", null);
        } else {
            Integer userId = (Integer) claim.getBody().get("id");
            result.put("chatRoomId", chatService.getChatRoomId(id, userId));
            result.put("isCheckedInterest", productInterestService.checkInterest(id, userId) == 1);
        }

        return result;
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Integer id,
                                                             @CookieValue(name = "x_auth", required = false) String token) {
        Map<String, Object> result = new HashMap<>();
        // TODO AOP 적용 또는 스프링 시큐리티 적용
        // jwt 유효성 검사
        Jws<Claims> claim = jwtService.decodeToken(token);
        if (claim == null) {
            result.put("message", "Not Authenticated");
            return ResponseEntity.status(401).body(result);
        }

        try {
            productService.deleteProductArticle(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Integer id,
                                                             @CookieValue(name = "x_auth", required = false) String token,
                                                             @Valid ProductArticleDto.ChangeInfo changeInfo) {
        Map<String, Object> result = new HashMap<>();
        // TODO AOP 적용 또는 스프링 시큐리티 적용
        // jwt 유효성 검사
        Jws<Claims> claim = jwtService.decodeToken(token);
        if (claim == null) {
            result.put("message", "Not Authenticated");
            return ResponseEntity.status(401).body(result);
        }

        try {
            productService.updateProductArticle(changeInfo, id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(path = "/{id}/interest")
    public ResponseEntity<Map<String, Object>> addProductInterestCount(
            @PathVariable Integer id,
            @CookieValue(name = "x_auth", required = false) String token) {
        Map<String, Object> result = new HashMap<>();
        Jws<Claims> claim = jwtService.decodeToken(token);
        if (claim == null) {
            result.put("isSuccess", false);
            result.put("message", "Not Authenticated");
            return ResponseEntity.status(401).body(result);
        }
        try {
            productInterestService.addInterestCount(id, (Integer) claim.getBody().get("id"));
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .build().toUri();
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = "/{id}/interest")
    public ResponseEntity<Map<String, Object>> subtractProductInterestCount(
            @PathVariable Integer id,
            @CookieValue(name = "x_auth", required = false) String token) {
        Map<String, Object> result = new HashMap<>();
        Jws<Claims> claim = jwtService.decodeToken(token);
        if (claim == null) {
            result.put("isSuccess", false);
            result.put("message", "Not Authenticated");
            return ResponseEntity.status(401).body(result);
        }
        try {
            productInterestService.subtractInterestCount(id, (Integer) claim.getBody().get("id"));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
