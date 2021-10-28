package com.hanium.product.domain.product;

import com.hanium.product.dto.RegisterProductDto;
import lombok.*;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author siru on 2021.10.10
 */
@Entity
@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(access =  AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
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
    private ProductArticleStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private LocalDateTime updateDate;

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
                .status(ProductArticleStatus.SALE)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    public void addProductImage(long fileId) {
        productImageList.add(ProductImage.builder()
                .productArticle(this)
                .listNum(getProductImageList().size() + 1)
                .fileId(fileId)
                .build());
    }

    public void addProductImages(List<Long> fileIdList) {
        fileIdList.forEach(fileId -> {
            productImageList.add(ProductImage.builder()
                    .productArticle(this)
                    .listNum(getProductImageList().size() + 1)
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

}
