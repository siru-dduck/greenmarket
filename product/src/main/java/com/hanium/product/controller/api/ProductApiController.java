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
import java.util.List;
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
        System.out.println(searchInfo);
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

    @PostMapping
    @AuthRequired
    public ResponseEntity<Map<String, Object>> postProduct(
            @Valid ProductArticleDto.RegisterInfo registerInfo,
            UserDto.Info user) {
        Map<String, Object> result = new HashMap<>();
        Integer articleId = productService.createProductArticle(registerInfo, user.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(articleId)
                .toUri();
        result.put("articleId", articleId);
        return ResponseEntity.created(location).body(result);
    }

    @GetMapping(path = "/{id}")
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

    @DeleteMapping(path = "/{articleId}")
    @AuthRequired
    public ResponseEntity<ProductArticleDto.Info> deleteProduct(@PathVariable Integer articleId,
                                                             UserDto.Info user) {
        productService.deleteProductArticle(articleId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{articleId}")
    @AuthRequired
    public ResponseEntity<ProductArticleDto.Info> updateProduct(@PathVariable Integer articleId,
                                                             UserDto.Info user,
                                                             @Valid ProductArticleDto.ChangeInfo changeInfo) throws Exception {
        productService.updateProductArticle(changeInfo, articleId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{id}/interest")
    @AuthRequired
    public ResponseEntity addProductInterestCount(@PathVariable Integer id,
                                                                       UserDto.Info user) {
        productInterestService.addInterest(id, user.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/{id}/interest")
    @AuthRequired
    public ResponseEntity subtractProductInterestCount(
            @PathVariable Integer id,
            UserDto.Info user) {
        productInterestService.removeInterest(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
