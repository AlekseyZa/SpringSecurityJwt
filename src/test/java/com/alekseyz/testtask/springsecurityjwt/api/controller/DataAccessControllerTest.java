package com.alekseyz.testtask.springsecurityjwt.api.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class DataAccessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Тестирование доступа к ресурсам админа с правами админа")
    void getAdminData_returnAdminData() {

        mockMvc.perform(get("/api/v1/admin")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Тестирование доступа к ресурсам пользователя с правами пользователя")
    void userData() {

        mockMvc.perform(get("/api/v1/user")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @WithAnonymousUser
    @DisplayName("Тестирование доступа к общедоступному ресурсу")
    void publicData() {

        mockMvc.perform(get("/api/v1/public")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }
}