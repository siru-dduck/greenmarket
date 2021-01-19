package com.hanium.product.service;

import com.hanium.product.dto.UserDto;

public interface UserService {
    UserDto.Info getUser(Integer userId);
}
