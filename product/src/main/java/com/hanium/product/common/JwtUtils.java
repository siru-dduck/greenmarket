package com.hanium.product.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class JwtUtils {
    private final String SECRET_TOKEN = "secret";

    private byte[] generateKey() {
        byte[] key = null;
        try {
            key = SECRET_TOKEN.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return key;
    }

    public Jws<Claims> decodeToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.generateKey())
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return null;
        }
    }
}
