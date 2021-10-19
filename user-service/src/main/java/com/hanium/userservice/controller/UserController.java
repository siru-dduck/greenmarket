package com.hanium.userservice.controller;

import com.hanium.userservice.controller.request.JoinRequest;
import com.hanium.userservice.controller.response.JoinResponse;
import com.hanium.userservice.dto.JoinDto;
import com.hanium.userservice.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
