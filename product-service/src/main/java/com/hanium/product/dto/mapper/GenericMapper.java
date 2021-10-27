package com.hanium.product.dto.mapper;

public interface GenericMapper<D, E> {
    E toEntity(D d);
    D toDto(E e);
}
