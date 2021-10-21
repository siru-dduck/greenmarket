package com.hanium.userservice.controller;

import com.hanium.userservice.domain.AuthUserDetail;
import com.hanium.userservice.dto.UpdateUserInfoDto;
import com.hanium.userservice.dto.UserInfoDto;
import com.hanium.userservice.dto.request.EmailValidationRequest;
import com.hanium.userservice.dto.request.JoinRequest;
import com.hanium.userservice.dto.request.UpdateUserInfoRequest;
import com.hanium.userservice.dto.response.EmailValidationResponse;
import com.hanium.userservice.dto.response.JoinResponse;
import com.hanium.userservice.dto.JoinDto;
import com.hanium.userservice.dto.response.UserInfoResponse;
import com.hanium.userservice.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    private static final String USERS_URL = "/users/{userId}";

    @ApiOperation(value = "회원가입", notes = "회원가입 api")
    @ApiResponses({
            @ApiResponse(code = 201, message = "ok status with user id, nickname"),
            @ApiResponse(code = 400, message = "bad request"),
            @ApiResponse(code = 409, message = "duplication user")
    })
    @PostMapping("/users/join")
    public ResponseEntity<JoinResponse> joinUser(@RequestBody @Valid JoinRequest joinRequest) {
        JoinDto joinInfo = modelMapper.map(joinRequest, JoinDto.class);
        long userId = userService.join(joinInfo);

        URI uri = URI.create(USERS_URL.replace("{userId}", String.valueOf(userId)));
        JoinResponse response = JoinResponse.builder()
                .userId(userId)
                .nickname(joinInfo.getNickname())
                .build();

        return ResponseEntity.created(uri).body(response);
    }

    @ApiOperation(value = "이메일 중복 체크", notes = "이메일 중복 체크 api")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok with email existence")
    })
    @GetMapping("/users/email/exist")
    public ResponseEntity<EmailValidationResponse> validateEmailDuplication(@Valid EmailValidationRequest emailValidationRequest) {
        // 이메일 중복체크
        String email = emailValidationRequest.getEmail();
        boolean isEmailExist = userService.checkEmailDuplication(email);

        // 응답
        EmailValidationResponse response = EmailValidationResponse.builder()
                .email(email)
                .result(isEmailExist ? EmailValidationResponse.Result.exist : EmailValidationResponse.Result.notExist)
                .build();
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "사용자 정보 조회", notes = "사용자 정보 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok with user info"),
            @ApiResponse(code = 404, message = "not found")
    })
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable long userId) {
        // 사용자 조회
        UserInfoDto findResult = userService.findUserById(userId);
        UserInfoResponse response = modelMapper.map(findResult, UserInfoResponse.class);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "사용자 정보 수정", notes = "사용자 정보 수정")
    @ApiResponses({
            @ApiResponse(code = 204, message = "no content"),
            @ApiResponse(code = 401, message = "not authentication"),
            @ApiResponse(code = 403, message = "access denied"),
            @ApiResponse(code = 404, message = "not found")
    })
    @PutMapping("/users/{userId}")
    public ResponseEntity<Void> updateUserInfo(@PathVariable long userId
            , @Valid @RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
        UpdateUserInfoDto updateUserInfo = modelMapper.map(updateUserInfoRequest, UpdateUserInfoDto.class);
        updateUserInfo.setUserId(userId);

        AuthUserDetail authUserDetail = (AuthUserDetail) SecurityContextHolder.getContext().getAuthentication().getDetails();

        // 사용자 정보 수정
        userService.updateUserInfo(updateUserInfo, authUserDetail);

        return ResponseEntity.noContent().build();
    }

    /**
     * TODO
     * 이메일 중복 체크 api (✅)
     * 사용자 정보 조회 api (✅)
     * 사용자 정보 수정 api (✅)
     * 사용자 리스트 조회 api ( )
     * 회원탈퇴 api ( )
     */
}
