package com.hanium.product.domain.product;

import com.hanium.product.dto.RegisterProductDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_ID", nullable = false)
    private Category category;

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

    public static ProductArticle createProductArticle(RegisterProductDto registerProductDto, Category category) {
        return ProductArticle.builder()
                .title(registerProductDto.getTitle())
                .content(registerProductDto.getContent())
                .category(category)
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

}
