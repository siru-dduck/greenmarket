package com.hanium.product.domain.product;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "product_image",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"product_id", "list_num"}
        )}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductArticle productArticle;

    @Column(name = "list_num", nullable = false)
    private Integer listNum;

    @Column(nullable = false)
    private Long fileId;

}
