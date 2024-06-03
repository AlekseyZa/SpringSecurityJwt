package com.alekseyz.testtask.springsecurityjwt.service;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationService {
    void refreshToken(HttpServletRequest request, HttpServletResponse response);

    AuthenticationUserResponseDto createAuthToken(
            @RequestBody AuthenticationUserRequestDto authenticationUserRequestDto);
}
