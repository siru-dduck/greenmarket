package com.hanium.product.service;

import com.hanium.product.dao.IProductArticleDao;
import com.hanium.product.dao.IProductImageDao;
import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.SearchInfoDto;
import com.hanium.product.repository.ProductArticleRepository;
import com.hanium.product.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductArticleRepository productArticleRepository;
    private final IProductArticleDao productArticleDao;
    private final IProductImageDao productImageDao;
    private final UserService userService;

    /**
     * 상품검색
     * @param searchInfo
     * @return
     */
    public List<ProductArticleDto> searchProductArticles(SearchInfoDto searchInfo) {
        return null;
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
    public Integer createProductArticle(ProductArticleDto.RegisterInfo registerInfo, Integer userId) {
//        ProductArticleDto.Info productArticle = ProductArticleDto.Info.builder()
//                .title(registerInfo.getTitle())
//                .content(registerInfo.getContent())
//                .price(registerInfo.getPrice())
//                .address1(registerInfo.getAddress1())
//                .address2(registerInfo.getAddress2())
//                .user(UserDto.Info.builder().id(userId).build())
//                .category(CategoryDto.builder().id(registerInfo.getCategoryId()).build())
//                .build();
//        productArticleDao.createBy(productArticle);
//
//        List<ProductImageDto> productImages = fileUtils.parseImageInfo(productArticle.getId(), registerInfo.getFiles());
//        productImageDao.createList(productImages);
//        return productArticle.getId();
        return null;
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