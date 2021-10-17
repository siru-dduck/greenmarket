package com.hanium.userservice.service;

import com.hanium.userservice.controller.dto.LoginDto;
import com.hanium.userservice.domain.RefreshToken;
import com.hanium.userservice.domain.User;
import com.hanium.userservice.domain.UserStatus;
import com.hanium.userservice.dto.AuthenticationDto;
import com.hanium.userservice.dto.JoinDto;
import com.hanium.userservice.exception.UserAuthenticationException;
import com.hanium.userservice.jwt.JwtProvider;
import com.hanium.userservice.model.UserDetail;
import com.hanium.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

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

    @Transactional
    public AuthenticationDto login(LoginDto loginDto) {
        // 사용자 조회
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new UserAuthenticationException("not found user"));

        // 패스워드 검증
        if(!user.validatePassword(loginDto.getPassword())) {
            throw new UserAuthenticationException("password is not correct");
        }

        // jwt 토큰 생성
        String jti = UUID.randomUUID().toString();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, new ArrayList<>());
        UserDetail userDetail = UserDetail.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .profileImageId(user.getProfileFileId())
                .tokenId(jti)
                .build();
        authentication.setDetails(userDetail);
        String accessTokenStr = jwtProvider.createAccessToken(authentication);
        String refreshTokenStr = jwtProvider.createAccessToken(authentication);

        // refresh token 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .tokenId(jti)
                .user(user)
                .token(refreshTokenStr)
                .createDate(LocalDateTime.now())
                .build();

        // 반환객체 생성
        AuthenticationDto authenticationResult = AuthenticationDto.builder()
                .accessToken(accessTokenStr)
                .refreshToken(refreshTokenStr)
                .build();

        return authenticationResult;
    }

    @Transactional
    public Long join(JoinDto joinDto) {
        User user = User.builder()
                .email(joinDto.getEmail())
                .address1(joinDto.getAddress1())
                .address2(joinDto.getAddress2())
                .nickname(joinDto.getNickname())
                .status(UserStatus.NORMAL)
                .creatDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        user.setPassword(joinDto.getPassword());
        userRepository.save(user);

        return user.getId();
    }
}