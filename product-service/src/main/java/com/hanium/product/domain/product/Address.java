package com.hanium.product.domain.product;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Address {

    @Column(length = 100 ,nullable = false)
    private String address1;

    @Column(length = 100 ,nullable = false)
    private String address2;

}
