package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import com.alekseyz.testtask.springsecurityjwt.service.AuthenticationService;
import com.alekseyz.testtask.springsecurityjwt.service.JwtTokenService;
import com.alekseyz.testtask.springsecurityjwt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationUserResponseDto createAuthToken(
            @RequestBody AuthenticationUserRequestDto authenticationUserRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationUserRequestDto.getUsername(),
                        authenticationUserRequestDto.getPassword()));
        User user = userService.findByUsername(authenticationUserRequestDto.getUsername())
                .orElseThrow();   //сделать проверку на отсутствие пользователя
        String accesToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        jwtTokenService.lockAnotherValidUserTokens(user);
        jwtTokenService.saveToken(user, accesToken);
        return AuthenticationUserResponseDto.builder()
                .accessToken(accesToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            jwtTokenService.refreshToken(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);        //сделать номральную обработку возможно пробросить
        }
    }
}
