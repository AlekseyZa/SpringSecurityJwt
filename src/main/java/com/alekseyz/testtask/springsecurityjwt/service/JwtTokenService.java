package com.alekseyz.testtask.springsecurityjwt.service;

import com.alekseyz.testtask.springsecurityjwt.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public interface JwtTokenService {



    String generateToken(User user);

    String generateAccessToken(Map<String, Object> extraClaims, User user);

    //проверяет валидность токена
    boolean isTokenValid(String token, User user);

    String extractUsername(String refreshToken);

    void lockAnotherValidUserTokens(User user);

    void saveToken(User user, String accesToken);

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;
}
