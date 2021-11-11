package com.hanium.product.config.security;

import com.hanium.product.config.properties.JwtProp;
import com.hanium.product.domain.user.AuthUserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProp jwtProp;

    private AuthUserDetail getUserDetail(Authentication authentication) {
        if(!(authentication.getDetails() instanceof AuthUserDetail)) {
            throw new IllegalStateException();
        }
        AuthUserDetail userDetail = (AuthUserDetail) authentication.getDetails();
        return userDetail;
    }

    public Jws<Claims> parseJwt(String token) {
        return Jwts
                .parser()
                .setSigningKey(jwtProp.getSecret().getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token);
    }


    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(jwtProp.getSecret().getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), token, new ArrayList<>());
        AuthUserDetail userDetail = AuthUserDetail.builder()
                .userId(claims.get("userId", Long.class))
                .email(claims.getSubject())
                .tokenId(claims.getId())
                .nickname(claims.get("nickname", String.class))
                .profileImageId(claims.get("profileImageId", Long.class))
                .build();
        authentication.setDetails(userDetail);

        return authentication;
    }

    public String createAccessToken(Authentication authentication) {
        AuthUserDetail userDetail = getUserDetail(authentication);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProp.getAccessTokenValidityInSeconds() * 1000);

        Claims claims = new DefaultClaims();
        claims.setId(userDetail.getTokenId());
        claims.setSubject(authentication.getName());
        claims.setIssuer("greenmarket");
        claims.setIssuedAt(now);
        claims.setExpiration(expiration);
        claims.put("userId", userDetail.getUserId());
        claims.put("profileImageId", userDetail.getProfileImageId());
        claims.put("nickname", userDetail.getNickname());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtProp.getSecret().getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        AuthUserDetail userDetail = getUserDetail(authentication);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProp.getRefreshTokenValidityInSeconds() * 1000);

        Claims claims = new DefaultClaims();
        claims.setId(userDetail.getTokenId());
        claims.setSubject(authentication.getName());
        claims.setIssuer("greenmarket");
        claims.setIssuedAt(now);
        claims.setExpiration(expiration);
        claims.put("userId", userDetail.getUserId());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtProp.getSecret().getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public boolean validateToken(String token) {
        boolean resultValue = true;

        String subject = null;

        try {
            subject = Jwts.parser().setSigningKey(jwtProp.getSecret().getBytes(StandardCharsets.UTF_8))
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
