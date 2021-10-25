package com.hanium.userservice.service;

import com.hanium.userservice.domain.AuthUserDetail;
import com.hanium.userservice.domain.RefreshToken;
import com.hanium.userservice.domain.User;
import com.hanium.userservice.dto.JoinDto;
import com.hanium.userservice.dto.LoginDto;
import com.hanium.userservice.dto.LoginResultDto;
import com.hanium.userservice.exception.UserAuthenticationException;
import com.hanium.userservice.config.security.jwt.JwtProvider;
import com.hanium.userservice.repository.RefreshTokenRepository;
import com.hanium.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SpringBootTest
@Transactional
public class UserAuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserAuthService userAuthService;

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

    private User createUser(String email, String password, String address1, String address2, String nickname) {
        JoinDto joinInfo= createJoinInfo(email, password, address1, address2, nickname);
        User user = User.createUser(joinInfo);
        return userRepository.save(user);
    }

    private AuthUserDetail login(String email, String password) {
        LoginDto loginInfo = LoginDto.builder()
                .email(email)
                .password(password)
                .build();

        LoginResultDto loginResult = userAuthService.login(loginInfo);
        Authentication authentication = jwtProvider.getAuthentication(loginResult.getAccessToken());
        return (AuthUserDetail) authentication.getDetails();
    }

    @Test
    public void 로그인_성공_테스트() throws Exception {
        // given
        String email = "test@email.com";
        String password = "password";
        createUser(email, password, "서울특별시", "강남구", "siru");

        LoginDto loginInfo = LoginDto.builder()
                .email(email)
                .password(password)
                .build();

        // when
        LoginResultDto loginResult = userAuthService.login(loginInfo);

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

        // when
        Throwable thrown = catchThrowable(()-> {
            userAuthService.login(loginInfo);
        });

        // then
        assertThat(thrown)
                .isInstanceOf(UserAuthenticationException.class)
                .hasMessage("not found user");
    }

    @Test
    public void 로그아웃_테스트() throws Exception {
        // given
        String email = "test@email.com";
        String password = "password";
        User user = createUser(email, password, "서울특별시", "강남구", "siru");
        AuthUserDetail authUserDetail = login(email, password);

        // when
        userAuthService.logout(authUserDetail);

        // then
        assertThat(refreshTokenRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void 토큰리프레시_테스트() throws Exception {
        // given
        String email = "test@email.com";
        String password = "password";
        createUser(email, password, "서울특별시", "강남구", "siru");

        AuthUserDetail authUserDetail = login(email, password);
        RefreshToken refreshToken = refreshTokenRepository.findByTokenId(authUserDetail.getTokenId());

        // when
        LoginResultDto refreshResult = userAuthService.refreshToken(refreshToken.getToken());

        // then
        assertThat(refreshTokenRepository.findAll().size()).isEqualTo(1);
    }
}
