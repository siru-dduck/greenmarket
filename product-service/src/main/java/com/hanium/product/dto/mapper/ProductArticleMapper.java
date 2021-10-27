package com.hanium.product.dto.mapper;

import com.hanium.product.domain.product.Address;
import com.hanium.product.domain.product.Category;
import com.hanium.product.domain.product.ProductArticle;
import com.hanium.product.domain.product.ProductImage;
import com.hanium.product.dto.*;
import com.hanium.product.dto.request.SearchRequest;
import com.hanium.product.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductArticleMapper {

    @Mapping(target = "offset", source = "offset", defaultValue = "0L")
    @Mapping(target = "size", source = "size", defaultValue = "20")
    SearchInfoDto map(SearchRequest searchRequest);

    Category map(CategoryDto categoryDto);

    @Mapping(target = "user", ignore = true)
    ProductArticleDto map(ProductArticle productArticleDto);

    @Mapping(target = "articleId", source = "productArticle.id")
    ProductImageDto map(ProductImage productImage);

    AddressDto map(Address address);

    ProductResponse map(ProductArticleDto productArticleDto);
}
