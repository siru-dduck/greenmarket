package siru.fileservice.controller.argumentResolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import siru.fileservice.domain.user.AuthUserDetail;
import siru.fileservice.exception.UserAuthenticationException;

public class SecurityArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == AuthUserDetail.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UserAuthenticationException("not found security authentication");
        }
        if (!(authentication.getDetails() instanceof AuthUserDetail)) {
            throw new IllegalStateException("auth detail type is illegal");
        }

        return (AuthUserDetail) authentication.getDetails();
    }
}
