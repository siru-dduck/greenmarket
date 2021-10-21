package com.hanium.userservice.controller;

import com.hanium.userservice.domain.AuthUserDetail;
import com.hanium.userservice.dto.request.LoginRequest;
import com.hanium.userservice.dto.response.LoginResponse;
import com.hanium.userservice.dto.LoginDto;
import com.hanium.userservice.dto.LoginResultDto;
import com.hanium.userservice.service.UserAuthService;
import com.hanium.userservice.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final UserAuthService userAuthService;
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
        LoginResultDto loginResult = userAuthService.login(loginDto);

        LoginResponse loginResponse = modelMapper.map(loginResult, LoginResponse.class);
        return ResponseEntity.ok(loginResponse);
    }

    @ApiOperation(value = "로그아웃", notes = "로그아웃 api")
    @ApiResponses({
            @ApiResponse(code = 204, message = "no content"),
            @ApiResponse(code = 401, message = "not authentication")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser() {
        // TODO authDetail 추출 로직 별도의 클래스와 메소드로 분리
        AuthUserDetail authUserDetail = (AuthUserDetail) SecurityContextHolder.getContext().getAuthentication().getDetails();
        userAuthService.logout(authUserDetail);
        return ResponseEntity.noContent().build();
    }

    /**
     * TODO
     * 로그아웃 api
     * jwt 유효성 검사 api
     * refresh token api
     * 비밀번호 찾기 api
     * 이메일 인증 api
     * 소셜로그인 카카오, 네이버
     */
}
