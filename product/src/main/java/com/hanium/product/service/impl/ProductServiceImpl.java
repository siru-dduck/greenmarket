package com.hanium.product.service.impl;

import com.hanium.product.common.FileUtils;
import com.hanium.product.dao.IProductArticleDao;
import com.hanium.product.dao.IProductImageDao;
import com.hanium.product.dto.CategoryDto;
import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.ProductImageDto;
import com.hanium.product.dto.UserDto;
import com.hanium.product.exception.AuthorizationException;
import com.hanium.product.exception.NotFoundResourceException;
import com.hanium.product.service.ProductService;
import com.hanium.product.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final IProductArticleDao productArticleDao;
    private final IProductImageDao productImageDao;
    private final UserService userService;
    private final FileUtils fileUtils;

    @Override
    public List<ProductArticleDto.Info> getProductArticles(ProductArticleDto.SearchInfo searchInfo) {
        return productArticleDao.findListBy(searchInfo);
    }

    @Override
    public ProductArticleDto.Info getProductArticle(Integer articleId) {
        ProductArticleDto.Info article =  productArticleDao.findOneBy(articleId);
        if(article == null) {
            throw new NotFoundResourceException("요청한 상품을 찾을 수 없습니다.");
        }
        UserDto.Info user = userService.getUser(article.getUser().getId());
        article.setUser(user);
        return article;
    }

    @Transactional
    @Override
    public Integer createProductArticle(ProductArticleDto.RegisterInfo registerInfo, Integer userId) {
        ProductArticleDto.Info productArticle = ProductArticleDto.Info.builder()
                .title(registerInfo.getTitle())
                .content(registerInfo.getContent())
                .price(registerInfo.getPrice())
                .address1(registerInfo.getAddress1())
                .address2(registerInfo.getAddress2())
                .user(UserDto.Info.builder().id(userId).build())
                .category(CategoryDto.builder().id(registerInfo.getCategoryId()).build())
                .build();
        productArticleDao.createBy(productArticle);

        List<ProductImageDto> productImages = fileUtils.parseImageInfo(productArticle.getId(), registerInfo.getFiles());
        productImageDao.createList(productImages);
        return productArticle.getId();
    }

    @Transactional
    @Override
    public void updateProductArticle(ProductArticleDto.ChangeInfo changeInfo, Integer articleId, Integer userId) {
        ProductArticleDto.Info article = productArticleDao.findOneBy(articleId);
        if(!article.getUser().getId().equals(userId)){
            throw new AuthorizationException("게시글 작성자만 글을 수정할 수 있습니다.");
        }

        productArticleDao.updateBy(changeInfo, articleId);
        productImageDao.deleteBy(articleId);
        List<ProductImageDto> productImages = fileUtils.parseImageInfo(articleId, changeInfo.getFiles());
        productImageDao.createList(productImages);
    }

    @Override
    public void deleteProductArticle(Integer articleId, Integer userId) {
        ProductArticleDto.Info article = productArticleDao.findOneBy(articleId);
        if(!article.getUser().getId().equals(userId)){
            throw new AuthorizationException("게시글 작성자만 글을 삭제할 수 있습니다.");
        }
        productArticleDao.deleteBy(articleId);
    }

}