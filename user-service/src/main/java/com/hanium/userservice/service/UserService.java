package com.hanium.userservice.service;

import com.hanium.userservice.domain.AuthUserDetail;
import com.hanium.userservice.domain.User;
import com.hanium.userservice.dto.*;
import com.hanium.userservice.exception.UserAlreadyExistException;
import com.hanium.userservice.exception.UserAuthenticationException;
import com.hanium.userservice.exception.UserAuthorizationException;
import com.hanium.userservice.exception.UserNotFoundException;
import com.hanium.userservice.jwt.JwtProvider;
import com.hanium.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author siru
 * 사용자 및 인증 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final ModelMapper modelMapper;

    /**
     * 로그인 & jwt 발행
     * @param loginDto
     * @return
     */
    @Transactional
    public LoginResultDto login(LoginDto loginDto) {
        // 사용자 조회
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UserAuthenticationException("not found user"));

        // 패스워드 검증
        if(!user.validatePassword(loginDto.getPassword())) {
            throw new UserAuthenticationException("password is not correct");
        }

        // jwt 토큰 생성
        Authentication authentication = user.createAuthentication();
        String accessTokenStr = jwtProvider.createAccessToken(authentication);
        String refreshTokenStr = jwtProvider.createRefreshToken(authentication);

        // refresh token 저장
        user.setRefreshToken(jwtProvider.parseJwt(refreshTokenStr), refreshTokenStr);

        // 반환객체 생성
        LoginResultDto loginResult = LoginResultDto.builder()
                .accessToken(accessTokenStr)
                .refreshToken(refreshTokenStr)
                .build();

        return loginResult;
    }

    /**
     * 회원가입
     * @param joinDto
     * @return
     */
    @Transactional
    public Long join(JoinDto joinDto) {
        // 중복 사용자 체크
        userRepository.findByEmail(joinDto.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistException(String.format("%s는 중복된 이메일 입니다.", joinDto.getEmail()));
                });

        // 회원가입
        User user = User.createUser(joinDto);
        userRepository.save(user);

        return user.getId();
    }

    /**
     * 이메일 존재 여부 체크
     * @param email
     * @return
     */
    public boolean checkEmailDuplication(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 사용자 정보 조회
     * @param userId
     * @return
     */
    public UserInfoDto findUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> { throw new UserNotFoundException("사용자를 찾을 수 없습니다."); });

        UserInfoDto userInfo = modelMapper.map(user, UserInfoDto.class);
        userInfo.setUserId(user.getId());
        return userInfo;
    }

    /**
     * 사용자 정보 수장
     * @param updateUserInfo
     * @return
     */
    @Transactional
    public UserInfoDto updateUserInfo(UpdateUserInfoDto updateUserInfo, AuthUserDetail authUserDetail) {
        User user = userRepository.findById(updateUserInfo.getUserId())
                .orElseThrow(() -> { throw new UserNotFoundException("사용자를 찾을 수 없습니다."); });

        // 권한 검사
        if(authUserDetail.getUserId() != user.getId()) {
            throw new UserAuthorizationException("권한이 없는 사용자 입니다.");
        }

        user.updateUserInfo(updateUserInfo);
        UserInfoDto userInfo = modelMapper.map(user, UserInfoDto.class);
        userInfo.setUserId(user.getId());
        return userInfo;
    }
}