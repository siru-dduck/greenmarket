package com.hanium.product.dto.mapper;

import com.hanium.product.dto.SearchInfoDto;
import com.hanium.product.dto.request.SearchRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SearchMapper {

    SearchInfoDto toDto(SearchRequest searchRequest);
}
