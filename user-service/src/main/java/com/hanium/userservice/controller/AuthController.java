package com.hanium.userservice.controller;

import com.hanium.userservice.domain.AuthUserDetail;
import com.hanium.userservice.dto.request.LoginRequest;
import com.hanium.userservice.dto.response.AuthUserInfoResponse;
import com.hanium.userservice.dto.response.LoginResponse;
import com.hanium.userservice.dto.LoginDto;
import com.hanium.userservice.dto.LoginResultDto;
import com.hanium.userservice.dto.response.RefreshTokenResponse;
import com.hanium.userservice.service.UserAuthService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
        // TODO authDetail 추출 로직 별도의 클래스와 메소드로 분리 => arguments resolver로 이걸?
        AuthUserDetail authUserDetail = (AuthUserDetail) SecurityContextHolder.getContext().getAuthentication().getDetails();
        userAuthService.logout(authUserDetail);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "jwt 인증체크 및 인증정보 조회", notes = "jwt 인증체크 및 인증정보 조회 api")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok with jwt auth info"),
            @ApiResponse(code = 401, message = "not authentication")
    })
    @GetMapping("/info")
    public ResponseEntity<AuthUserInfoResponse> authInfo() {
        // TODO authDetail 추출 로직 별도의 클래스와 메소드로 분리 => arguments resolver로 이걸?
        AuthUserDetail authUserDetail = (AuthUserDetail) SecurityContextHolder.getContext().getAuthentication().getDetails();

        AuthUserInfoResponse response = modelMapper.map(authUserDetail, AuthUserInfoResponse.class);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "jwt 리프레시", notes = "jwt 리프레시 api")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok with jwt auth info"),
            @ApiResponse(code = 401, message = "not authentication")
    })
    @PostMapping("/refreshToken")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestHeader("x-user-refresh-token") String refreshToken) {
        LoginResultDto refreshResult = userAuthService.refreshToken(refreshToken);

        RefreshTokenResponse response = modelMapper.map(refreshResult, RefreshTokenResponse.class);
        return ResponseEntity.ok(response);
    }

    /**
     * TODO
     * 로그아웃 api(✅)
     * jwt 유효성 검사 인증정보 api (✅)
     * refresh token api (✅)
     * 비밀번호 찾기 api
     * 이메일 인증 api
     * 소셜로그인 카카오, 네이버
     */
}
