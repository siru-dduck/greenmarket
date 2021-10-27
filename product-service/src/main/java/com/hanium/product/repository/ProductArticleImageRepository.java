package com.hanium.product.repository;

import com.hanium.product.domain.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductArticleImageRepository extends JpaRepository<ProductImage, Long> {

    @Query("select i from ProductImage i where i.productArticle.id in :productIdList and i.listNum = 1 order by i.productArticle.id, i.listNum")
    List<ProductImage> findMainImageByFileIdIn(@Param("productIdList") List<Long> productIdList);

}