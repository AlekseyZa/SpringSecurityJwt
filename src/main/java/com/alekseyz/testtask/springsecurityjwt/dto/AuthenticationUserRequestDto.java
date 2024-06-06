package com.alekseyz.testtask.springsecurityjwt.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Набор данных отправляемых при аутентификации")
@Data
public class AuthenticationUserRequestDto {

    @Schema(description = "Логин")
    private String username;
    @Schema(description = "Пароль")
    private String password;
}
