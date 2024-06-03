package com.alekseyz.testtask.springsecurityjwt.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationUserResponseDto {

    String accessToken;
    String refreshToken;
}
