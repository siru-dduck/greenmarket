package com.hanium.userservice.controller;

import com.hanium.userservice.controller.request.LoginRequest;
import com.hanium.userservice.controller.response.LoginResponse;
import com.hanium.userservice.dto.LoginDto;
import com.hanium.userservice.dto.LoginResultDto;
import com.hanium.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        LoginDto loginDto  = modelMapper.map(loginRequest, LoginDto.class);
        LoginResultDto loginResult = userService.login(loginDto);

        LoginResponse loginResponse = modelMapper.map(loginResult, LoginResponse.class);
        return ResponseEntity.ok(loginResponse);
    }
}
