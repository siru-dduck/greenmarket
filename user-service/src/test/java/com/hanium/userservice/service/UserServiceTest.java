package com.hanium.userservice.service;

import com.hanium.userservice.domain.AuthUserDetail;
import com.hanium.userservice.domain.User;
import com.hanium.userservice.domain.UserStatus;
import com.hanium.userservice.dto.*;
import com.hanium.userservice.config.security.jwt.JwtProvider;
import com.hanium.userservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

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

    @Test
    public void 사용자리스트_조회() throws Exception {
        // given
        User user1 = createUser("test1@email.com", "password", "서울특별시", "강남구", "siru1");
        User user2 = createUser("test2@email.com", "password", "서울특별시", "성북구", "siru2");
        User user3 = createUser("test3@email.com", "password", "부산광역시", "해운대구", "siru3");
        User user4 = createUser("test4@email.com", "password", "성남시", "수지구", "siru4");
        User user5 = createUser("test5@email.com", "password", "인천광역시", "부평구", "siru5");

        List<Long> userIdList = Stream.of(user1,user2,user3,user4,user5)
                .map(User::getId)
                .collect(Collectors.toList());

        // when
        List<UserInfoDto> userInfoList = userService.findUserList(userIdList);

        // then
        assertThat(userInfoList.size()).isEqualTo(5);
    }

    @Test
    public void 회원탈퇴_테스트() throws Exception {
        // given
        User user = createUser("test@email.com", "password", "서울특별시", "강남구", "siru");
        AuthUserDetail authUserDetail = login("test@email.com", "password");

        // when
        userService.deleteAccount(user.getId(), authUserDetail);
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> { throw new IllegalStateException(); });

        // then
        assertThat(findUser.getRefreshTokenList().size()).isEqualTo(0);
        assertThat(findUser.getStatus()).isEqualTo(UserStatus.DELETE_ACCOUNT);
    }
}