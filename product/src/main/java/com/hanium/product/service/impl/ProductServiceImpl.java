package com.hanium.product.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.hanium.product.dto.ProductArticleRequestDto;
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
                                                      Integer userId, String order, Integer offset, Integer limit, List<Integer> articleIds) {
        return productArticleDao.findList(keyword, address1, address2, userId, order, offset,
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

    @Transactional
    @Override
    public Integer createProductArticle(ProductArticleDto productArticle, List<MultipartFile> multipartFiles)
            throws Exception {
        try {
            productArticleDao.createBy(productArticle);
            List<ProductImageDto> productImages = multipartFiles.stream().map(f -> ProductImageDto.builder()
                    .articleId(productArticle.getId()).fileUrl("/resources/images/" + f.getOriginalFilename()).build())
                    .collect(Collectors.toList());
            productImageDao.createList(productImages);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        for (MultipartFile multipartFile : multipartFiles) {
            File targetFile = new File(RESOURCE_FILE_PATH + multipartFile.getOriginalFilename());
            try {
                InputStream fileStream = multipartFile.getInputStream();
                FileUtils.copyInputStreamToFile(fileStream, targetFile);
            } catch (IOException e) {
                FileUtils.deleteQuietly(targetFile);
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }

        return productArticle.getId();
    }

    @Override
    public void updateProductArticle(ProductArticleRequestDto productArticleRequestDto, Integer id) {
        productArticleDao.updateBy(productArticleRequestDto, id);
        // TODO 파일처리를 위한 FileUtils 작성
    }

    @Override
    public void deleteProductArticle(Integer id) {
        productArticleDao.deleteBy(id);
    }

}