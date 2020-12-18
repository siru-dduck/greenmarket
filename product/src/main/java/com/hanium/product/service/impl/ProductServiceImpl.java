package com.hanium.product.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hanium.product.dao.IProductArticleDao;
import com.hanium.product.dao.IProductImageDao;
import com.hanium.product.dto.ProductArticleDto;
import com.hanium.product.dto.ProductImageDto;
import com.hanium.product.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    @Value("${resource.file.path}")
    private String RESOURCE_FILE_PATH;
    private final IProductArticleDao productArticleDao;
    private final IProductImageDao productImageDao;

    public ProductServiceImpl(IProductArticleDao productArticleDao, IProductImageDao productImageDao) {
        this.productArticleDao = productArticleDao;
        this.productImageDao = productImageDao;
    }

    @Override
    public List<ProductArticleDto> getProductArticles(String keyword, String address1, String address2,
                                                      Integer userId, String interestCount, Integer offset, Integer limit, List<Integer> articleIds) {
        return productArticleDao.findList(keyword, address1, address2, userId, interestCount, offset,
                offset + limit, articleIds);
    }

    @Override
    public ProductArticleDto getProductArticle(Integer articleId) {
        return productArticleDao.findBy(articleId);
    }

    @Override
    public List<ProductImageDto> getProductImages(Integer articleId) {
        return productImageDao.findList(articleId);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Integer writeProductArticle(ProductArticleDto productArticle, List<MultipartFile> multipartFiles)
            throws Exception {
        try {
            productArticleDao.create(productArticle);
            List<ProductImageDto> productImages = multipartFiles.stream().map(f -> ProductImageDto.builder()
                    .articleId(productArticle.getId()).fileUrl("/resources/images/" + f.getOriginalFilename()).build())
                    .collect(Collectors.toList());
            productImageDao.createList(productImages);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        for (MultipartFile multipartFile : multipartFiles) {
            File targetFile = new File(RESOURCE_FILE_PATH + multipartFile.getOriginalFilename());
            try {
                InputStream fileStream = multipartFile.getInputStream();
                FileUtils.copyInputStreamToFile(fileStream, targetFile);
            } catch (IOException e) {
                FileUtils.deleteQuietly(targetFile);
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
        }

        return productArticle.getId();
    }
}
