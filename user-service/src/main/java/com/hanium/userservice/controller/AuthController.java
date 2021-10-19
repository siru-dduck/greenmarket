package com.hanium.userservice.controller;

import com.hanium.userservice.dto.request.LoginRequest;
import com.hanium.userservice.dto.response.LoginResponse;
import com.hanium.userservice.dto.LoginDto;
import com.hanium.userservice.dto.LoginResultDto;
import com.hanium.userservice.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @ApiOperation(value = "로그인", notes = "로그인 api")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok status with user id, nickname"),
            @ApiResponse(code = 400, message = "bad request"),
            @ApiResponse(code = 401, message = "not authentication")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        LoginDto loginDto  = modelMapper.map(loginRequest, LoginDto.class);
        LoginResultDto loginResult = userService.login(loginDto);

        LoginResponse loginResponse = modelMapper.map(loginResult, LoginResponse.class);
        return ResponseEntity.ok(loginResponse);
    }
}
