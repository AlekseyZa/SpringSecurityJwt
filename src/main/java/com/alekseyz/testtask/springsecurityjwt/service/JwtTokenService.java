package com.alekseyz.testtask.springsecurityjwt.service;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface JwtTokenService {

    boolean isTokenValid(String token, User user);

    String getUsername(String token);

    void lockAnotherValidUserTokens(User user);

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    AuthenticationUserResponseDto refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;

    void saveToken(User user, String token, String type);
}
