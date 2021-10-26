package com.hanium.product.dto.mapper;

import com.hanium.product.dto.SearchInfoDto;
import com.hanium.product.dto.request.SearchRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductArticleMapper {

    @Mapping(target = "offset", source = "offset", defaultValue = "0L")
    @Mapping(target = "size", source = "size", defaultValue = "20")
    SearchInfoDto toDto(SearchRequest searchRequest);
}
