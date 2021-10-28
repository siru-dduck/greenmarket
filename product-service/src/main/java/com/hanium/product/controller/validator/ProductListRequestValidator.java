package com.hanium.product.controller.validator;

import com.hanium.product.dto.request.ProductListRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductListRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductListRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductListRequest request = (ProductListRequest) target;
        if (CollectionUtils.isEmpty(request.getProductIdList()) && request.getUserId() == null) {
            errors.rejectValue("userId", "empty", "userId와 productIdList 중 반드시 하나는 입력해야 합니다.");
            errors.rejectValue("productIdList", "empty", "userId와 productIdList 중 반드시 하나는 입력해야 합니다.");
        }
    }
}
