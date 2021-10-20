package com.hanium.userservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanium.userservice.dto.request.EmailValidationRequest;
import com.hanium.userservice.dto.response.EmailValidationResponse;
import com.hanium.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void 이메일_중복_테스트() throws Exception {
        // given
        String email = "test@email.com";
        EmailValidationRequest validationRequest = EmailValidationRequest.builder().email(email).build();

        // when
        given(userService.checkEmailDuplication(any())).willReturn(false);
        ResultActions notExistResult = requestValidateEmailDuplication(validationRequest); // 이메일이 존재하지 않는 경우

        given(userService.checkEmailDuplication(any())).willReturn(true);
        ResultActions existResult = requestValidateEmailDuplication(validationRequest); // 이메일이 존재하는 경우

        // then
        notExistResult
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").value(EmailValidationResponse.Result.notExist.name()))
                .andExpect(jsonPath("email").value(email));

        existResult
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").value(EmailValidationResponse.Result.exist.name()))
                .andExpect(jsonPath("email").value(email));
    }

    private ResultActions requestValidateEmailDuplication(EmailValidationRequest emailValidationRequest) throws Exception {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        Map<String, String> maps = objectMapper.convertValue(emailValidationRequest, new TypeReference<Map<String, String>>() {});
        queryParams.setAll(maps);

        return mvc.perform(get("/users/email/exist")
                        .queryParams(queryParams))
                .andDo(print());
    }

}