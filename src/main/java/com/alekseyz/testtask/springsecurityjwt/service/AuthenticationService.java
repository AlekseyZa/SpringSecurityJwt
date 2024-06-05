package com.alekseyz.testtask.springsecurityjwt.service;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;

public interface AuthenticationService {

    AuthenticationUserResponseDto authentication(AuthenticationUserRequestDto authenticationUserRequestDto);

    AuthenticationUserResponseDto refreshToken(String refreshToken);
}
