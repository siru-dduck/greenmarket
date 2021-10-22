package com.hanium.userservice.service;

import com.hanium.userservice.domain.AuthUserDetail;
import com.hanium.userservice.domain.User;
import com.hanium.userservice.dto.LoginDto;
import com.hanium.userservice.dto.LoginResultDto;
import com.hanium.userservice.exception.UserAuthenticationException;
import com.hanium.userservice.exception.UserNotFoundException;
import com.hanium.userservice.jwt.JwtProvider;
import com.hanium.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author siru
 * 사용자 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthService {

    private final UserRepository userRepository;
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
}
