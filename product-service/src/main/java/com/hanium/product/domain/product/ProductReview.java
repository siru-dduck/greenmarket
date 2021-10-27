package com.hanium.product.domain.product;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(access =  AccessLevel.PROTECTED)
public class ProductReview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_article_id", unique = true)
    private ProductArticle productArticle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

}
