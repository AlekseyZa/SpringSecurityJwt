package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import com.alekseyz.testtask.springsecurityjwt.exceptionhandling.UserException;
import com.alekseyz.testtask.springsecurityjwt.service.AuthenticationService;
import com.alekseyz.testtask.springsecurityjwt.service.JwtTokenService;
import com.alekseyz.testtask.springsecurityjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationUserResponseDto authentication(AuthenticationUserRequestDto authenticationUserRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationUserRequestDto.getUsername(),
                        authenticationUserRequestDto.getPassword()));
        User user = userService.findByUsername(authenticationUserRequestDto.getUsername()).orElseThrow(() ->
                new UserException("Пользователь с таким логином не найден: " + authenticationUserRequestDto.getUsername()));
        jwtTokenService.lockAnotherValidUserTokens(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        String accesToken = jwtTokenService.generateAccessToken(user);
        jwtTokenService.saveToken(user, refreshToken, "refresh");
        return AuthenticationUserResponseDto.builder()
                .accessToken(accesToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationUserResponseDto refreshToken(@NonNull String refreshToken) {
        return jwtTokenService.refreshToken(refreshToken);
    }
}
