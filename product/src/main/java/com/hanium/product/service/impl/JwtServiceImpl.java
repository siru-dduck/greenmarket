package com.hanium.product.service.impl;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Service;

import com.hanium.product.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Service
public class JwtServiceImpl implements JwtService {

    private byte[] generateKey() {
        byte[] key = null;
        try {
            key = SECRET_TOKEN.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return key;
    }

    @Override
    public Jws<Claims> decodeToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(this.generateKey())
                    .parseClaimsJws(token);
            return claims;
        } catch (Exception e) {
            return null;
        }
    }

}
