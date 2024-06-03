package com.alekseyz.testtask.springsecurityjwt.dto;

import lombok.Data;

@Data
public class RegistrationUserRequestDto {

    private String username;
    private String password;
    private String confirmPassword;
}
