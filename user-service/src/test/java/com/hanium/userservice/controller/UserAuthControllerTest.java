package com.hanium.userservice.controller;

import com.hanium.userservice.domain.User;
import com.hanium.userservice.dto.JoinDto;
import com.hanium.userservice.dto.LoginDto;
import com.hanium.userservice.dto.LoginResultDto;
import com.hanium.userservice.config.security.jwt.JwtProvider;
import com.hanium.userservice.repository.UserRepository;
import com.hanium.userservice.service.UserAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserAuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

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

    private LoginResultDto login(String email, String password) {
        LoginDto loginDto = LoginDto.builder()
                .email(email)
                .password(password)
                .build();
        return userAuthService.login(loginDto);
    }

    @Test
    public void jwt_인증정보_조회_테스트() throws Exception {
        // given
        String email = "test@email.com";
        String password = "password";
        User user = createUser(email, password, "서울특별시", "강남구", "siru");
        LoginResultDto loginResult = login(email, password);
        String jti = jwtProvider.parseJwt(loginResult.getAccessToken()).getBody().getId();

        // when
        ResultActions result = requestJwtAuthInfo(loginResult.getAccessToken());

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("nickname").value(user.getNickname()))
                .andExpect(jsonPath("userId").value(user.getId()))
                .andExpect(jsonPath("email").value(user.getEmail()))
                .andExpect(jsonPath("tokenId").value(jti))
                .andExpect(jsonPath("profileImageId").value(user.getProfileFileId()));
    }

    private ResultActions requestJwtAuthInfo(String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        return mvc.perform(get("/auth/info")
                        .headers(headers))
                .andDo(print());
    }
}
