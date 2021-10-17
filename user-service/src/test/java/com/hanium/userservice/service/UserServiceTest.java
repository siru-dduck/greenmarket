package com.hanium.userservice.service;

import com.hanium.userservice.config.properties.JwtProp;
import com.hanium.userservice.controller.dto.LoginDto;
import com.hanium.userservice.domain.User;
import com.hanium.userservice.domain.UserStatus;
import com.hanium.userservice.dto.AuthenticationDto;
import com.hanium.userservice.dto.JoinDto;
import com.hanium.userservice.exception.UserAuthenticationException;
import com.hanium.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProp jwtProp;

    @Test
    public void 회원가입_성공_테스트() throws Exception {
        // given
        JoinDto joinInfo = JoinDto.builder()
                .email("test@email.com")
                .password("password")
                .address1("서울특별시")
                .address2("강남구")
                .nickname("siru")
                .build();

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
        User user = User.builder()
                .email(email)
                .address1("서울특별시")
                .address2("강남구")
                .nickname("siru")
                .status(UserStatus.NORMAL)
                .creatDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        user.setPassword(password);
        userRepository.save(user);

        LoginDto loginInfo = LoginDto.builder()
                .email(email)
                .password(password)
                .build();

        // when
        AuthenticationDto loginResult = userService.login(loginInfo);
        String accessToken = loginResult.getAccessToken();
        String refreshToken = loginResult.getRefreshToken();
        Claims accessTokenClaims = parseJwt(accessToken);
        Claims refreshTokenClaims = parseJwt(refreshToken);

        // then
        Assertions.assertEquals(email, accessTokenClaims.getSubject());
        Assertions.assertEquals(email, refreshTokenClaims.getSubject());
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

    private Claims parseJwt(String token) {
        return Jwts
                .parser()
                .setSigningKey(jwtProp.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }
}