package com.hanium.product.controller.api;

import com.hanium.product.common.AuthRequired;
import com.hanium.product.dto.CategoryDto;
import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.UserDto;
import com.hanium.product.service.ChatService;
import com.hanium.product.service.ProductInterestService;
import com.hanium.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/products")
@AllArgsConstructor
public class ProductApiController {
    private final ProductService productService;
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
    @AuthRequired
    public ResponseEntity<Map<String, Object>> postProduct(
            @Valid ProductArticleDto.RegisterInfo registerInfo,
            UserDto user) throws Exception {
        Map<String, Object> result = new HashMap<>();
        ProductArticleDto.Info productArticle = ProductArticleDto.Info.builder()
                .title(registerInfo.getTitle())
                .content(registerInfo.getContent())
                .price(registerInfo.getPrice())
                .user(UserDto.builder().id(user.getId()).build())
                .category(CategoryDto.builder().id(registerInfo.getCategoryId()).build())
                .build();
        Integer articleId = productService.createProductArticle(productArticle, registerInfo.getFiles());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(articleId)
                .toUri();
        result.put("articleId", articleId);
        return ResponseEntity.created(location).body(result);
    }

    @GetMapping(path = "/{id}")
    public Map<String, Object> getProduct(@PathVariable Integer id,
                                          UserDto user) {
        Map<String, Object> result = new HashMap<>();
        result.put("productArticle", productService.getProductArticle(id));
        result.put("productImages", productService.getProductImages(id));

        if (user == null) {
            result.put("chatRoomId", null);
        } else {
            Integer userId = user.getId();
            result.put("chatRoomId", chatService.getChatRoomId(id, userId));
            result.put("isCheckedInterest", productInterestService.isCheckedInterest(id, userId));
        }
        return result;
    }

    @DeleteMapping(path = "/{articleId}")
    @AuthRequired
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Integer articleId,
                                                             UserDto user) {
        productService.deleteProductArticle(articleId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{articleId}")
    @AuthRequired
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Integer articleId,
                                                             UserDto user,
                                                             @Valid ProductArticleDto.ChangeInfo changeInfo) throws Exception {
        productService.updateProductArticle(changeInfo, articleId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{id}/interest")
    @AuthRequired
    public ResponseEntity<Map<String, Object>> addProductInterestCount(
            @PathVariable Integer id,
            UserDto user) {
        productInterestService.addInterest(id, user.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/{id}/interest")
    @AuthRequired
    public ResponseEntity<Map<String, Object>> subtractProductInterestCount(
            @PathVariable Integer id,
            UserDto user) {
        productInterestService.removeInterest(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
