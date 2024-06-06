package com.alekseyz.testtask.springsecurityjwt.api.controller;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;
    private final static ObjectMapper objectMapper =new ObjectMapper();
    private final String accessToken = "jiwenioh.nwejqwjd23093jf903.4fh23904f";
    private final String refreshToken = "fkwpojkgf-wefkp[w.p2wjgvop2j0.pfnmqweopfmnkl";


    @BeforeEach
    void setUp() {

    }

    @SneakyThrows
    @Test
    @DisplayName("Тестирование утентификации и получения ответа с токенами")
    void authenticateUserBYLoginPassword_returnNewJwtTokens() {

        AuthenticationUserRequestDto authenticationUserRequestDto =
                AuthenticationUserRequestDto.builder()
                        .username("user")
                        .password("password")
                        .build();
        AuthenticationUserResponseDto authenticationUserResponseDto =
                AuthenticationUserResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

        when(authenticationService.authentication(authenticationUserRequestDto))
                .thenReturn(authenticationUserResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationUserResponseDto)))
                .andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    @DisplayName("Тестирование обновления токена доступа и получения ответа с обновленным токеном")
    void refreshAccessTokenByRefreshToken_returnUpdatedJwtTokens() {

        AuthenticationUserResponseDto authenticationUserResponseDto =
                AuthenticationUserResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

        when(authenticationService.refreshToken(refreshToken)).thenReturn(authenticationUserResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationUserResponseDto)))
                .andExpect(status().isOk());

    }
}