package com.alekseyz.testtask.springsecurityjwt.service;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

public interface JwtTokenService {

    void lockAnotherValidUserTokens(User user);

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    AuthenticationUserResponseDto refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;

    void saveToken(User user, String token, String type);

    String getUsername(String jwtToken);

    boolean isTokenValid(String jwtToken, UserDetails userDetails);


}
