package com.hanium.product.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hanium.product.dto.CategoryDto;
import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.UserDto;
import com.hanium.product.service.ChatService;
import com.hanium.product.service.JwtService;
import com.hanium.product.service.ProductService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@RestController
@RequestMapping(path = "/api/products")
public class ProductApiController {
	@Autowired
	private ProductService productService;
	@Autowired
	private JwtService jwtSerivce;
	@Autowired
	private ChatService chatService;
	
	@GetMapping
	public Map<String, Object> getProductList(@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "address1", required = false) String address1,
			@RequestParam(name = "address2", required = false) String address2,
			@RequestParam(name = "user_id", required = false) Integer userId,
			@RequestParam(name = "order", required = false) String order,
			@RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset,
			@RequestParam(name = "limit", required = false, defaultValue = "20") Integer limit,
			@RequestParam(name = "article_ids", required = false) List<Integer> articleIds) {
		Map<String, Object> result = new HashMap<>();

		result.put("productArticles", productService.getProductArticles(keyword, address1, address2, 
				userId,order, offset, limit, articleIds));
		return result;
	}

	@GetMapping(path = "/{id}")
	public Map<String, Object> getProduct(@PathVariable Integer id,
			@CookieValue(name = "x_auth", required = false) String token) {
		Map<String, Object> result = new HashMap<>();
		result.put("productArticle", productService.getProductArticle(id));
		result.put("productImages", productService.getProductImages(id));
		// jwt 유효성 검사
		Jws<Claims> claim = jwtSerivce.decodeToken(token);
		if (claim == null) {
			result.put("chatRoomId", null);
		} else {
			result.put("chatRoomId", chatService.getChatRoomId(id, (Integer)claim.getBody().get("id")));
		}
		return result;
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> postProduct(
			@RequestParam(name = "file") List<MultipartFile> multipartFiles, @RequestParam String title,
			@RequestParam Integer price, @RequestParam String content, @RequestParam Integer category,
			@CookieValue(name = "x_auth", required = false) String token) {
		Map<String, Object> result = new HashMap<>();
		System.out.println(title + " " + price + " " + content + " " + multipartFiles);

		// validation
		if (multipartFiles.size() <= 0 || title.trim().length() <= 0 || (price <= 0 || price >= 100000000)
				|| content.trim().length() <= 0) {
			result.put("isSuccess", false);
			result.put("message", "Bad Request");
			return ResponseEntity.status(400).body(result);
		}

		// jwt 유효성 검사
		Jws<Claims> claim = jwtSerivce.decodeToken(token);
		if (claim == null) {
			result.put("isSuccess", false);
			result.put("message", "Not Authenticated");
			return ResponseEntity.status(401).body(result);
		}
			
		try {
			ProductArticleDto article = ProductArticleDto.builder().title(title).content(content).price(price)
					.user(UserDto.builder().id((Integer) claim.getBody().get("id")).build())
					.category(CategoryDto.builder().id(category).build()).build();
			result.put("articleId", productService.writeProductArticle(article, multipartFiles));
			result.put("isSuccess", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("isSuccess", false);
		}
		return ResponseEntity.ok().body(result);

	}
}
