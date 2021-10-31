package com.hanium.product.domain.product;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "product_interest",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"product_article_id", "user_id"}
        )}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(access = AccessLevel.PROTECTED)
public class ProductInterest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_article_id", nullable = false)
    private ProductArticle productArticle;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    public static ProductInterest createProductInterest(ProductArticle product, long userId) {
        return ProductInterest.builder()
                .productArticle(product)
                .userId(userId)
                .createDate(LocalDateTime.now())
                .build();
    }

}
