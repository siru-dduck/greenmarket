package com.hanium.product.service.impl;

import com.hanium.product.common.FileUtils;
import com.hanium.product.dao.IProductArticleDao;
import com.hanium.product.dao.IProductImageDao;
import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.ProductArticleRequestDto;
import com.hanium.product.dto.ProductImageDto;
import com.hanium.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final IProductArticleDao productArticleDao;
    private final IProductImageDao productImageDao;
    private final FileUtils fileUtils;

    @Override
    public List<ProductArticleDto.Info> getProductArticles(ProductArticleDto.SearchInfo searchInfo) {
        return productArticleDao.findList(searchInfo.getKeyword(), searchInfo.getAddress1(), searchInfo.getAddress2(),
                searchInfo.getUserId(), searchInfo.getOrder(), searchInfo.getOffset(), searchInfo.getLimit(), searchInfo.getArticleIds());
    }

    @Override
    public ProductArticleDto.Info getProductArticle(Integer articleId) {
        return productArticleDao.findBy(articleId);
    }

    @Override
    public List<ProductImageDto> getProductImages(Integer articleId) {
        return productImageDao.findList(articleId);
    }

    @Transactional
    @Override
    public Integer createProductArticle(ProductArticleDto.Info productArticle, List<MultipartFile> multipartFiles) throws Exception {
        productArticleDao.createBy(productArticle);
        List<ProductImageDto> productImages = fileUtils.parseImageInfo(productArticle.getId(), multipartFiles);
        if(productImages == null) {
            throw new RuntimeException("NotSavedImageFiles");
        }
        productImageDao.createList(productImages);
        return productArticle.getId();
    }

    @Transactional
    @Override
    public void updateProductArticle(ProductArticleDto.ChangeInfo changeInfo, Integer articleId) throws Exception {
        productArticleDao.updateBy(changeInfo, articleId);
        productImageDao.deleteBy(articleId);
        List<ProductImageDto> productImages = fileUtils.parseImageInfo(articleId, changeInfo.getFiles());
        productImageDao.createList(productImages);
    }

    @Override
    public void deleteProductArticle(Integer id) {
        productArticleDao.deleteBy(id);
    }

}