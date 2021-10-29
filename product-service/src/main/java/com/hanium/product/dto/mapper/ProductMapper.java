package com.hanium.product.dto.mapper;

import com.hanium.product.domain.product.*;
import com.hanium.product.dto.*;
import com.hanium.product.dto.request.ProductListRequest;
import com.hanium.product.dto.request.RegisterProductRequest;
import com.hanium.product.dto.request.SearchRequest;
import com.hanium.product.dto.request.UpdateProductRequest;
import com.hanium.product.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "offset", source = "offset", defaultValue = "0L")
    @Mapping(target = "size", source = "size", defaultValue = "20")
    SearchInfoDto map(SearchRequest searchRequest);

    FindProductListDto map(ProductListRequest productListRequest);

    UpdateProductDto map(UpdateProductRequest updateProductRequest);

    Category map(CategoryDto categoryDto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "productImageList", ignore = true)
    ProductArticleDto map(ProductArticle productArticle);

    @Mapping(target = "articleId", source = "productArticle.id")
    ProductImageDto map(ProductImage productImage);

    AddressDto map(Address address);

    @Mapping(target = "imageFileIdList", source = "productImageList", ignore = true)
    @Mapping(target = "isCheckInterest", ignore = true)
    ProductResponse map(ProductArticleDto productArticleDto);

    RegisterProductDto map(RegisterProductRequest registerProductRequest);
}
