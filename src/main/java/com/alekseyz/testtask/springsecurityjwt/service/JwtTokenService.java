package com.alekseyz.testtask.springsecurityjwt.service;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenService {

    AuthenticationUserResponseDto refreshToken(String refreshToken);

    void lockAnotherValidUserTokens(User user);

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);


    void saveToken(User user, String token, String type);

    String getUsername(String jwtToken);

    boolean isAccessTokenValid(String jwtAccessToken);
}
