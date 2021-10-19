package com.hanium.userservice.jwt;

import com.hanium.userservice.config.properties.JwtProp;
import com.hanium.userservice.domain.UserDetail;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProp jwtProp;

    private UserDetail getUserDetail(Authentication authentication) {
        if(!(authentication.getDetails() instanceof UserDetail)) {
            throw new IllegalStateException();
        }
        UserDetail userDetail = (UserDetail) authentication.getDetails();
        return userDetail;
    }

    public Jws<Claims> parseJwt(String token) {
        return Jwts
                .parser()
                .setSigningKey(jwtProp.getSecret())
                .parseClaimsJws(token);
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

    public String createAccessToken(Authentication authentication) {
        UserDetail userDetail = getUserDetail(authentication);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProp.getAccessTokenValidityInSeconds() * 1000);

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

    public String createRefreshToken(Authentication authentication) {
        UserDetail userDetail = getUserDetail(authentication);

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
                .signWith(SignatureAlgorithm.HS256, jwtProp.getSecret())
                .compact();
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
