package com.hanium.userservice.controller;

import com.hanium.userservice.controller.request.LoginRequest;
import com.hanium.userservice.controller.response.LoginResponse;
import com.hanium.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        LoginDto loginDto  =

        AuthenticationDto loginResult = userService.login(loginDto);
    }
}
