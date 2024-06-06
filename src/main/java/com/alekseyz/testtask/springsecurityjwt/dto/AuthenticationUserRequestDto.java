package com.alekseyz.testtask.springsecurityjwt.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Набор данных отправляемых при аутентификации")
@Builder
@Data
public class AuthenticationUserRequestDto {

    @Schema(description = "Логин")
    String username;
    @Schema(description = "Пароль")
    String password;
}
