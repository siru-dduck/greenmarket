package com.hanium.product.controller.api;

import com.hanium.product.dto.ProductArticleDto;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;

public class ProductArticleSearchInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final Integer DEFAULT_OFFSET_VALUE = 0;
    private final Integer DEFAULT_LIMIT_VALUE = 20;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(ProductArticleDto.SearchInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ProductArticleDto.SearchInfo searchInfo = new ProductArticleDto.SearchInfo();
        String offsetStr = webRequest.getParameter("offset");
        String limitStr = webRequest.getParameter("limit");


        if(offsetStr == null){
            searchInfo.setOffset(DEFAULT_OFFSET_VALUE);
        }
        if(limitStr == null){
            searchInfo.setLimit(DEFAULT_LIMIT_VALUE);
        }

        return searchInfo;
    }
}
