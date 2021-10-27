package com.hanium.product.service;

import com.hanium.product.dao.IProductArticleDao;
import com.hanium.product.dao.IProductImageDao;
import com.hanium.product.domain.product.Category;
import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.ProductImage;
import com.hanium.product.domain.product.QProductArticle;
import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.RegisterProductDto;
import com.hanium.product.dto.SearchInfoDto;
import com.hanium.product.dto.mapper.ProductArticleMapper;
import com.hanium.product.exception.InvalidCategoryId;
import com.hanium.product.repository.CategoryRepository;
import com.hanium.product.repository.ProductArticleImageRepository;
import com.hanium.product.repository.ProductArticleRepository;
import com.hanium.product.repository.querydsl.ProductArticlePredicatesBuilder;
import com.hanium.product.repository.querydsl.SearchOperation;
import com.hanium.product.service.UserService;
import com.querydsl.jpa.impl.JPAQuery;
import io.undertow.predicate.PathMatchPredicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductArticleRepository productArticleRepository;
    private final ProductArticleImageRepository productArticleImageRepository;
    private final ProductArticleMapper productArticleMapper;
    private final IProductArticleDao productArticleDao;
    private final IProductImageDao productImageDao;
    private final UserService userService;
    private final CategoryRepository categoryRepository;

    /**
     * 상품검색
     * @param searchInfo
     * @return
     */
    public List<ProductArticleDto> searchProductArticles(SearchInfoDto searchInfo) {
        List<ProductArticle> productArticleList = productArticleRepository.findBySearchQuery(searchInfo);
        Map<ProductArticle, ProductImage> productMainImageMap = productArticleImageRepository.findMainImageByFileIdIn(
                productArticleList.stream()
                        .map(ProductArticle::getId)
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(ProductImage::getProductArticle, Function.identity()));
        List<ProductArticleDto> searchResult = new ArrayList<>();
        productArticleList.forEach(productArticle -> {
            ProductArticleDto productArticleInfo = productArticleMapper.map(productArticle);
            productArticleInfo.setProductImageList(List.of(productArticleMapper.map(productMainImageMap.get(productArticle))));
            searchResult.add(productArticleInfo);
        });
        return searchResult;
    }

    /**
     * 상품 리스트 조회
     * @param searchInfo
     * @return
     */
    public List<ProductArticleDto.Info> getProductArticles(ProductArticleDto.SearchInfo searchInfo) {
        return productArticleDao.findListBy(searchInfo);
    }

    /**
     * 상품 게시글 조회
     * @param articleId
     * @return
     */
    public ProductArticleDto.Info getProductArticle(Integer articleId) {
//        ProductArticleDto.Info article =  productArticleDao.findOneBy(articleId);
//        if(article == null) {
//            throw new NotFoundResourceException("요청한 상품을 찾을 수 없습니다.");
//        }
//        UserDto.Info user = userService.getUser(article.getUser().getId());
//        article.setUser(user);
//        return article;
        return null;
    }

    /**
     * 상품등록
     * @param registerInfo
     * @param userId
     * @return
     */
    @Transactional
    public long registerProductArticle(RegisterProductDto registerInfo, long userId) {
        Category category = categoryRepository.findById(registerInfo.getCategoryId())
                .orElseThrow(() -> new InvalidCategoryId(String.format("invalid category id %d", registerInfo.getCategoryId())));
        ProductArticle product = ProductArticle.createProductArticle(registerInfo, category, userId);
        product.addProductImages(registerInfo.getFileIdList());
        productArticleRepository.save(product);
        return product.getId();
    }

    /**
     * 상품 게시글 수정
     * @param changeInfo
     * @param articleId
     * @param userId
     */
    @Transactional
    public void updateProductArticle(ProductArticleDto.ChangeInfo changeInfo, Integer articleId, Integer userId) {
//        ProductArticleDto.Info article = productArticleDao.findOneBy(articleId);
//        if(!article.getUser().getId().equals(userId)){
//            throw new AuthorizationException("게시글 작성자만 글을 수정할 수 있습니다.");
//        }
//
//        productArticleDao.updateBy(changeInfo, articleId);
//        productImageDao.deleteBy(articleId);
//        List<ProductImageDto> productImages = fileUtils.parseImageInfo(articleId, changeInfo.getFiles());
//        productImageDao.createList(productImages);
    }

    /**
     * 상품 게시글 삭제
     * @param articleId
     * @param userId
     */
    @Transactional
    public void deleteProductArticle(Integer articleId, Integer userId) {
//        ProductArticleDto.Info article = productArticleDao.findOneBy(articleId);
//        if(!article.getUser().getId().equals(userId)){
//            throw new AuthorizationException("게시글 작성자만 글을 삭제할 수 있습니다.");
//        }
//        productArticleDao.deleteBy(articleId);
    }

}