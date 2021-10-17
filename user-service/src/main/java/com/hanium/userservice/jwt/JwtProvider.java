package com.hanium.userservice.jwt;

import com.hanium.userservice.config.properties.JwtProp;
import com.hanium.userservice.model.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.JwtMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProp jwtProp;


    public String createToken(Authentication authentication) {
        if(!(authentication.getDetails() instanceof UserDetail)) {
            throw new IllegalStateException();
        }

        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProp.getTokenValidityInSeconds());

        UserDetail userDetail = (UserDetail) authentication.getDetails();
        Claims claims = new DefaultClaims();
        claims.setSubject(authentication.getName());
        claims.setIssuer("greenmarket");
        claims.setIssuedAt(now);
        claims.setExpiration(expiration);
        claims.put("userId", userDetail.getUserId());
        claims.put("profileImageId", userDetail.getProfileImageId());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtProp.getSecret())
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(jwtProp.getSecret())
                .parseClaimsJws(token)
                .getBody();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), token, new ArrayList<>());
        UserDetail userDetail = UserDetail.builder()
                .userId(claims.get("userId", Long.class))
                .email(claims.getSubject())
                .profileImageId(claims.get("profileImageId", Long.class))
                .build();
        authentication.setDetails(userDetail);

        return authentication;
    }

    public boolean validateToken(String token) {
        boolean resultValue = true;

        String subject = null;

        try {
            subject = Jwts.parser().setSigningKey(jwtProp.getSecret())
                    .parseClaimsJws(token).getBody()
                    .getSubject();
        } catch (Exception ex) {
            resultValue = false;
        }

        if (StringUtils.isBlank(subject)) {
            resultValue = false;
        }

        return resultValue;
    }
}
