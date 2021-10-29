package com.hanium.product.domain.product;

import com.hanium.product.dto.RegisterProductDto;
import com.hanium.product.dto.UpdateProductDto;
import com.hanium.product.exception.ProductAlreadyDeleteException;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author siru on 2021.10.10
 */
@Entity
@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(access =  AccessLevel.PROTECTED)
public class ProductArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "review_id")
    private ProductReview productReview;

    @OneToMany(mappedBy = "productArticle", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ProductImage> productImageList = new ArrayList<>();

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int interestCount;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private LocalDateTime updateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductArticle that = (ProductArticle) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    /**
     * 엔티티 생성 메소드
     * @param registerProductDto
     * @param category
     * @param userId
     * @return
     */
    public static ProductArticle createProductArticle(RegisterProductDto registerProductDto, Category category, long userId) {
        return ProductArticle.builder()
                .title(registerProductDto.getTitle())
                .content(registerProductDto.getContent())
                .category(category)
                .userId(userId)
                .address(Address.builder()
                        .address1(registerProductDto.getAddress1())
                        .address2(registerProductDto.getAddress2())
                        .build())
                .price(registerProductDto.getPrice())
                .interestCount(0)
                .status(ProductStatus.SALE)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    /*************************************************
     * 비즈니스 로직
     *************************************************/

    public void addProductImage(long fileId) {
        productImageList.add(ProductImage.builder()
                .productArticle(this)
                .listNum(getProductImageList().size() + 1)
                .fileId(fileId)
                .build());
    }

    public void setProductImages(List<Long> fileIdList) {
        getProductImageList().clear();
        fileIdList.forEach(fileId -> {
            productImageList.add(ProductImage.builder()
                    .productArticle(this)
                    .listNum(productImageList.size() + 1)
                    .fileId(fileId)
                    .build());
        });
    }

    public ProductImage getMainProductImage() {
        if(CollectionUtils.isEmpty(productImageList)) {
            return null;
        }
        return productImageList.get(0);
    }

    public void deleteProduct() {
        if(status == ProductStatus.DELETE) {
            throw new ProductAlreadyDeleteException("이미 삭제된 상품입니다.");
        }
        status = ProductStatus.DELETE;
    }

    public void updateProduct(UpdateProductDto updateInfo, Category category) {
        if(status == ProductStatus.DELETE) {
            throw new ProductAlreadyDeleteException("이미 삭제된 상품입니다.");
        }
        title = updateInfo.getTitle();
        content = updateInfo.getContent();
        price = updateInfo.getPrice();
        address = Address.builder()
                .address1(updateInfo.getAddress1())
                .address2(updateInfo.getAddress2())
                .build();
        this.category = category;


    }
}
