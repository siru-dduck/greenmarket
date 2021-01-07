package com.hanium.product.controller.api;

import com.hanium.product.dto.CategoryDto;
import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.ProductArticleRequestDto;
import com.hanium.product.dto.UserDto;
import com.hanium.product.service.ChatService;
import com.hanium.product.service.JwtService;
import com.hanium.product.service.ProductInterestService;
import com.hanium.product.service.ProductService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/products")
public class ProductApiController {

    private final ProductService productService;
    private final JwtService jwtService;
    private final ChatService chatService;
    private final ProductInterestService productInterestService;

    public ProductApiController(ProductService productService, JwtService jwtService, ChatService chatService, ProductInterestService productInterestService) {
        this.productService = productService;
        this.jwtService = jwtService;
        this.chatService = chatService;
        this.productInterestService = productInterestService;
    }

    // TODO RequestParam을 DTO로 추상화하기
    @GetMapping
    public Map<String, Object> getProductList(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "address1", required = false) String address1,
            @RequestParam(name = "address2", required = false) String address2,
            @RequestParam(name = "user_id", required = false) Integer userId,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(name = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(name = "article_ids", required = false) List<Integer> articleIds) {
        Map<String, Object> result = new HashMap<>();

        result.put("productArticles", productService.getProductArticles(keyword, address1, address2,
                userId, order, offset, limit, articleIds));
        return result;
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
            result.put("isSuccess", false);
            result.put("message", "Not Authenticated");
            return ResponseEntity.status(401).body(result);
        }

        try {
            productService.deleteProductArticle(id);
            result.put("isSuccess", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("isSuccess", false);
        }
        return ResponseEntity.ok().body(result);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Integer id,
                                                             @CookieValue(name = "x_auth", required = false) String token,
                                                             ProductArticleRequestDto productArticleRequestDto) {
        Map<String, Object> result = new HashMap<>();
        // TODO AOP 적용 또는 스프링 시큐리티 적용
        // jwt 유효성 검사
        Jws<Claims> claim = jwtService.decodeToken(token);
        if (claim == null) {
            result.put("isSuccess", false);
            result.put("message", "Not Authenticated");
            return ResponseEntity.status(401).body(result);
        }
        
        try {
            productService.updateProductArticle(productArticleRequestDto, id);
            result.put("isSuccess", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("isSuccess", false);
        }
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> postProduct(
            @RequestParam(name = "files") List<MultipartFile> multipartFiles, @RequestParam String title,
            @RequestParam Integer price, @RequestParam String content, @RequestParam Integer categoryId,
            @CookieValue(name = "x_auth", required = false) String token) {
        Map<String, Object> result = new HashMap<>();

        // TODO validation 적용하기
        // validation
        if (multipartFiles.size() <= 0 || title.trim().length() <= 0 || (price <= 0 || price >= 100000000)
                || content.trim().length() <= 0) {
            result.put("isSuccess", false);
            result.put("message", "Bad Request");
            return ResponseEntity.status(400).body(result);
        }

        // TODO AOP 적용 또는 스프링 시큐리티 적용
        // jwt 유효성 검사
        Jws<Claims> claim = jwtService.decodeToken(token);
        if (claim == null) {
            result.put("isSuccess", false);
            result.put("message", "Not Authenticated");
            return ResponseEntity.status(401).body(result);
        }

        try {
            ProductArticleDto article = ProductArticleDto.builder().title(title).content(content).price(price)
                    .user(UserDto.builder().id((Integer) claim.getBody().get("id")).build())
                    .category(CategoryDto.builder().id(categoryId).build()).build();
            result.put("articleId", productService.createProductArticle(article, multipartFiles));
            result.put("isSuccess", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("isSuccess", false);
        }
        return ResponseEntity.ok().body(result);
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
            result.put("isSuccess", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("isSuccess", false);
        }
        return ResponseEntity.ok().body(result);
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
            result.put("isSuccess", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("isSuccess", false);
        }
        return ResponseEntity.ok().body(result);
    }

}
