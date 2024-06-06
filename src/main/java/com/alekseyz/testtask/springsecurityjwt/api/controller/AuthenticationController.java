package com.alekseyz.testtask.springsecurityjwt.api.controller;


import com.alekseyz.testtask.springsecurityjwt.api.AuthenticationApi;
import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.dto.RefreshTokenDto;
import com.alekseyz.testtask.springsecurityjwt.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationApi {

    private final AuthenticationService authenticationService;

    @Override
    public AuthenticationUserResponseDto authentication(
            @RequestBody AuthenticationUserRequestDto authenticationUserRequestDto) {
        return authenticationService.authentication(authenticationUserRequestDto);
    }

    @Override
    public AuthenticationUserResponseDto refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        return authenticationService.refreshToken(refreshTokenDto.getRefreshToken());
    }
}
