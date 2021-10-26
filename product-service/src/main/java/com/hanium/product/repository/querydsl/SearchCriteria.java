package com.hanium.product.repository.querydsl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;
}