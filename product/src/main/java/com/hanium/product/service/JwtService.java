package com.hanium.product.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface JwtService {
	public final String SECRET_TOKEN = "secret";
	public Jws<Claims> decodeToken(String token);
}
