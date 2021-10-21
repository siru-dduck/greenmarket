package com.hanium.userservice.service;

import com.hanium.userservice.domain.AuthUserDetail;
import com.hanium.userservice.domain.RefreshToken;
import com.hanium.userservice.domain.User;
import com.hanium.userservice.domain.UserStatus;
import com.hanium.userservice.dto.*;
import com.hanium.userservice.exception.UserAuthenticationException;
import com.hanium.userservice.jwt.JwtProvider;
import com.hanium.userservice.repository.RefreshTokenRepository;
import com.hanium.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.*;

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

    private User createUser(String email, String password, String address1, String address2, String nickname) {
        JoinDto joinInfo= createJoinInfo(email, password, address1, address2, nickname);
        User user = User.createUser(joinInfo);
        return userRepository.save(user);
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
        createUser(email, password, "서울특별시", "강남구", "siru");

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

        // when
        Throwable thrown = catchThrowable(()-> {
            userService.login(loginInfo);
        });

        // then
        assertThat(thrown)
                .isInstanceOf(UserAuthenticationException.class)
                .hasMessage("not found user");
    }

    @Test
    public void 이메일_중복테스트() throws Exception {
        // given
        String email = "test@email.com";
        JoinDto joinInfo = createJoinInfo(email, "password", "서울특별시", "강남구", "siru");
        User user = User.createUser(joinInfo);
        userRepository.save(user);

        // when
        boolean emailExistResult = userService.checkEmailDuplication(email);
        boolean emailNotExistResult = userService.checkEmailDuplication("notExistEmail~");

        // then
        Assertions.assertTrue(emailExistResult);
        Assertions.assertFalse(emailNotExistResult);
    }

    @Test
    public void 사용자_조회_테스트() throws Exception {
        // given
        User user = createUser("test@email.com", "password", "서울특별시", "강남구", "siru");

        // when
        UserInfoDto userInfo = userService.findUserById(user.getId());

        // then
        assertThat(userInfo.getUserId()).isEqualTo(user.getId());
        assertThat(userInfo.getNickname()).isEqualTo(user.getNickname());
        assertThat(userInfo.getAddress1()).isEqualTo(user.getAddress1());
        assertThat(userInfo.getAddress2()).isEqualTo(user.getAddress2());
    }
    
    @Test
    public void 사용자정보_수정_테스트() throws Exception {
        // given
        User user = createUser("test@email.com", "password", "서울특별시", "강남구", "siru");
        AuthUserDetail authUserDetail = AuthUserDetail.builder()
                .userId(user.getId())
                .build();

        String updateAddress1 = "인천광역시";
        String updateAddress2 = "연수구";
        String updateNickname = "시루";
        long updateProfileFileId = 1;
        UpdateUserInfoDto updateUserInfo = UpdateUserInfoDto.builder()
                .userId(user.getId())
                .address1(updateAddress1)
                .address2(updateAddress2)
                .nickname(updateNickname)
                .profileFileId(updateProfileFileId)
                .build();


        // when
        UserInfoDto userInfo = userService.updateUserInfo(updateUserInfo, authUserDetail);

        // then
        assertThat(userInfo.getUserId()).isEqualTo(user.getId());
        assertThat(userInfo.getAddress1()).isEqualTo(updateAddress1);
        assertThat(userInfo.getAddress2()).isEqualTo(updateAddress2);
        assertThat(userInfo.getNickname()).isEqualTo(updateNickname);
        assertThat(userInfo.getProfileFileId()).isEqualTo(updateProfileFileId);
    }

}