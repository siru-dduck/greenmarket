package com.hanium.userservice.controller;

import com.hanium.userservice.dto.request.EmailValidationRequest;
import com.hanium.userservice.dto.request.JoinRequest;
import com.hanium.userservice.dto.response.EmailValidationResponse;
import com.hanium.userservice.dto.response.JoinResponse;
import com.hanium.userservice.dto.JoinDto;
import com.hanium.userservice.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @ApiOperation(value = "회원가입", notes = "회원가입 api")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok status with user id, nickname"),
            @ApiResponse(code = 400, message = "bad request"),
            @ApiResponse(code = 409, message = "duplication user")
    })
    @PostMapping("/users/join")
    public ResponseEntity<JoinResponse> joinUser(@RequestBody @Valid JoinRequest joinRequest) {
        JoinDto joinInfo = modelMapper.map(joinRequest, JoinDto.class);
        long userId = userService.join(joinInfo);

        JoinResponse response = JoinResponse.builder()
                .userId(userId)
                .nickname(joinInfo.getNickname())
                .build();

        return ResponseEntity.ok(response);
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

    /**
     * TODO
     * 이메일 중복 체크 api (✅)
     * 사용자 정보 조회 api
     * 사용자 정보 수정 api
     * 사용자 리스트 조회 api
     * 회원탈퇴 api
     */
}
