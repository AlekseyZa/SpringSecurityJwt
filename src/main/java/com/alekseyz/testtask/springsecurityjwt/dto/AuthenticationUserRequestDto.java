package com.alekseyz.testtask.springsecurityjwt.dto;


import lombok.Data;


@Data
public class AuthenticationUserRequestDto {

    private String username;
    private String password;
}
