package com.hanium.product.service.impl;

import com.hanium.product.dto.UserDto;
import com.hanium.product.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final RestTemplate restTemplate;
    @Value("${service.user.host}")
    private String SERVICE_USER_HOST;
    @Value("${service.user.port}")
    private String SERVICE_USER_PORT;

    @Override
    public UserDto.Info getUser(Integer userId) {
        final String baseUrl = "http://" + SERVICE_USER_HOST +":" + SERVICE_USER_PORT + "/api/user/{userId}";
        Map<String, Integer> query = new HashMap<>();
        query.put("userId", userId);
        ResponseEntity<UserDto.ResponseInfo> response = restTemplate.getForEntity(baseUrl, UserDto.ResponseInfo.class, query);
        return response.getBody().getUser();
    }
}
