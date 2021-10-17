package com.hanium.userservice.controller;

import com.hanium.userservice.controller.dto.LoginDto;
import com.hanium.userservice.controller.request.LoginRequest;
import com.hanium.userservice.controller.response.LoginResponse;
import com.hanium.userservice.dto.AuthenticationDto;
import com.hanium.userservice.service.UserService;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        AuthenticationDto loginResult = userService.login(loginDto);

        LoginResponse loginResponse = modelMapper.map(loginResult, LoginResponse.class);
        return ResponseEntity.ok(loginResponse);
    }
}
