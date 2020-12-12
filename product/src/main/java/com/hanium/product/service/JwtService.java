package com.hanium.product.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface JwtService {
	String SECRET_TOKEN = "secret";
	Jws<Claims> decodeToken(String token);
}
