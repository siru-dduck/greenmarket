package com.hanium.userservice.mapper;

import org.mapstruct.Mapping;

public interface GenericMapper<D, E> {
    D toDto(E e);
    E toEntity(D d);
}