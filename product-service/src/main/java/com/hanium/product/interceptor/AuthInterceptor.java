package com.hanium.product.interceptor;

import com.hanium.product.common.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    private final JwtUtils jwtUtils;
    private final static String AUTH_TOKEN_NAME = "x_auth";

    public AuthInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            if (hm.hasMethodAnnotation(AuthRequired.class)) {
                Cookie authCookie = WebUtils.getCookie(request, AUTH_TOKEN_NAME);
                if (authCookie == null) {
                    throw new AuthenticationException("Not Authenticated");
                } else {
                    Claims claims = jwtUtils.decodeToken(authCookie.getValue()).getBody();
                    if (claims == null) {
                        throw new AuthenticationException("Not Authenticated");
                    }
                }
            }
        }
        return true;
    }
}
