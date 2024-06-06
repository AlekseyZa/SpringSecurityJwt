package com.alekseyz.testtask.springsecurityjwt.api.controller;

import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    private final static ObjectMapper objectMapper =new ObjectMapper();

    @SneakyThrows
    @Test
    @DisplayName("Тестирование регистрации пользователя")
    void registrationUser_returnLogin() {
        RegistrationUserRequestDto registrationUserRequestDto =
                RegistrationUserRequestDto.builder()
                        .username("user")
                        .password("password")
                        .confirmPassword("password")
                        .build();
        RegistrationUserResponseDto registrationUserResponseDto =
                RegistrationUserResponseDto.builder()
                        .id(1L)
                        .username("user")
                        .build();

        when(registrationService.registration(registrationUserRequestDto))
                .thenReturn(registrationUserResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationUserResponseDto)))
                .andExpect(status().isOk());
    }

}