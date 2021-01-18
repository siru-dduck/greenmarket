package com.hanium.product.argumentresolver;

import com.hanium.product.common.JwtUtils;
import com.hanium.product.dto.UserDto;
import com.hanium.product.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class UserTokenArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtUtils jwtUtils;
    private final static String AUTH_TOKEN_NAME = "x_auth";

    public UserTokenArgumentResolver(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == UserDto.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie authCookie = WebUtils.getCookie(request, AUTH_TOKEN_NAME);
        if (authCookie != null) {
            Claims claims = jwtUtils.decodeToken(authCookie.getValue()).getBody();
            if (claims != null) {
                return UserDto.builder()
                        .id(claims.get("id", Integer.class))
                        .build();
            }
        }
        return null;
    }
}
