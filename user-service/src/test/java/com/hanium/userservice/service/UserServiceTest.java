package com.hanium.userservice.service;

import com.hanium.userservice.domain.RefreshToken;
import com.hanium.userservice.domain.User;
import com.hanium.userservice.domain.UserStatus;
import com.hanium.userservice.dto.JoinDto;
import com.hanium.userservice.dto.LoginDto;
import com.hanium.userservice.dto.LoginResultDto;
import com.hanium.userservice.exception.UserAuthenticationException;
import com.hanium.userservice.jwt.JwtProvider;
import com.hanium.userservice.repository.RefreshTokenRepository;
import com.hanium.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    private JoinDto createJoinInfo(String email, String password, String address1, String address2, String nickname) {
        return JoinDto.builder()
                .email(email)
                .password(password)
                .address1(address1)
                .address2(address2)
                .nickname(nickname)
                .build();
    }

    @Test
    public void 회원가입_성공_테스트() throws Exception {
        // given
        JoinDto joinInfo = createJoinInfo("test@email.com", "password", "서울특별시", "강남구", "siru");

        // when
        long joinUserId = userService.join(joinInfo);
        User user = userRepository.findById(joinUserId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // then
        Assertions.assertEquals("test@email.com", user.getEmail());
        Assertions.assertTrue(user.validatePassword("password"));
        Assertions.assertEquals("서울특별시", user.getAddress1());
        Assertions.assertEquals("강남구", user.getAddress2());
        Assertions.assertEquals(UserStatus.NORMAL, user.getStatus());
    }

    @Test
    public void 로그인_성공_테스트() throws Exception {
        // given
        String email = "test@email.com";
        String password = "password";
        JoinDto joinInfo = createJoinInfo(email, password, "서울특별시", "강남구", "siru");
        User user = User.createUser(joinInfo);
        userRepository.save(user);

        LoginDto loginInfo = LoginDto.builder()
                .email(email)
                .password(password)
                .build();

        // when
        LoginResultDto loginResult = userService.login(loginInfo);

        // then
        String accessToken = loginResult.getAccessToken();
        String refreshToken = loginResult.getRefreshToken();
        Claims accessTokenClaims = jwtProvider.parseJwt(accessToken).getBody();
        Claims refreshTokenClaims = jwtProvider.parseJwt(refreshToken).getBody();

        RefreshToken findRefreshToken = refreshTokenRepository.findByTokenId(refreshTokenClaims.getId());
        Assertions.assertEquals(email, accessTokenClaims.getSubject());
        Assertions.assertEquals(email, refreshTokenClaims.getSubject());
        Assertions.assertNotNull(findRefreshToken);
        Assertions.assertEquals(refreshToken, findRefreshToken.getToken());
    }

    @Test
    public void 존재하지않는_회원로그인_실패_테스트() throws Exception {
        // given
        LoginDto loginInfo = LoginDto.builder()
                .email("notExist@email.test")
                .password("1234567890")
                .build();

        // when then
        Assertions.assertThrows(UserAuthenticationException.class
                , () -> userService.login(loginInfo)
                , "not found user!");
    }

}