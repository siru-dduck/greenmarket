package com.hanium.product.domain.product;

import javax.persistence.*;

@Entity
@Table(
        name = "product_image",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"product_article_id", "list_num"}
        )}
)
public class ProductImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_article_id", nullable = false)
    private ProductArticle productArticle;

    @Column(name = "list_num", nullable = false)
    private Integer listNum;

    @Column(nullable = false)
    private Long fileId;
}
