package com.hanium.userservice.service;

import com.hanium.userservice.domain.AuthUserDetail;
import com.hanium.userservice.domain.User;
import com.hanium.userservice.dto.LoginDto;
import com.hanium.userservice.dto.LoginResultDto;
import com.hanium.userservice.exception.UserAuthenticationException;
import com.hanium.userservice.exception.UserNotFoundException;
import com.hanium.userservice.config.security.jwt.JwtProvider;
import com.hanium.userservice.repository.RefreshTokenRepository;
import com.hanium.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author siru
 * 사용자 서비스
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

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
        LoginResultDto loginResult = authToken(user);

        return loginResult;
    }

    /**
     * 로그아웃
     * @param authUserDetail
     */
    @Transactional
    public void logout(AuthUserDetail authUserDetail) {
        // 사용자 조회
        User user = userRepository.findById(authUserDetail.getUserId())
                .orElseThrow(() -> { throw new UserNotFoundException("사용자를 찾을 수 없습니다."); });

        // refresh token 만료
        user.expireRefreshToken(authUserDetail.getTokenId());
    }

    /**
     * jwt 리프래시
     * @param refreshToken
     */
    @Transactional
    public LoginResultDto refreshToken(String refreshToken) {
        // 토큰 유효성 검사
        if(!jwtProvider.validateToken(refreshToken)) {
            throw new UserAuthenticationException("invalid token");
        }

        // 토큰 정보 파싱
        Claims claims = jwtProvider.parseJwt(refreshToken).getBody();
        Long userId = claims.get("userId", Long.class);
        String jti = claims.getId();

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> { throw new UserNotFoundException("사용자를 찾을 수 없습니다."); });

        // 기존 리프래시 토큰 파기
        user.expireRefreshToken(jti);

        // jwt 토큰 생성
        LoginResultDto loginResult = authToken(user);

        return loginResult;
    }

    private LoginResultDto authToken(User user) {
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
}
