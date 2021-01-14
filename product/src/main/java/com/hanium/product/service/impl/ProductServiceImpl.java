package com.hanium.product.service.impl;

import com.hanium.product.common.FileUtils;
import com.hanium.product.dao.IProductArticleDao;
import com.hanium.product.dao.IProductImageDao;
import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.ProductArticleRequestDto;
import com.hanium.product.dto.ProductImageDto;
import com.hanium.product.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final IProductArticleDao productArticleDao;
    private final IProductImageDao productImageDao;
    private final FileUtils fileUtils;

    public ProductServiceImpl(IProductArticleDao productArticleDao, IProductImageDao productImageDao, FileUtils fileUtils) {
        this.productArticleDao = productArticleDao;
        this.productImageDao = productImageDao;
        this.fileUtils = fileUtils;
    }

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
        productImageDao.createList(productImages);
        return productArticle.getId();
    }

    @Transactional
    @Override
    public void updateProductArticle(ProductArticleRequestDto productArticleRequestDto, Integer articleId) throws Exception {
        productArticleDao.updateBy(productArticleRequestDto, articleId);
        productImageDao.deleteBy(articleId);
        List<ProductImageDto> productImages = fileUtils.parseImageInfo(articleId, productArticleRequestDto.getFiles());
        productImageDao.createList(productImages);
    }

    @Override
    public void deleteProductArticle(Integer id) {
        productArticleDao.deleteBy(id);
    }

}