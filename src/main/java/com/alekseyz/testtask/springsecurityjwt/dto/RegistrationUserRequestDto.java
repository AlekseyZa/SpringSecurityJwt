package com.alekseyz.testtask.springsecurityjwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Набор данных отправляемых при регистрации")
@Builder
@Data
public class RegistrationUserRequestDto {

    @Schema(description = "Логин")
    private String username;
    @Schema(description = "Пароль")
    private String password;
    @Schema(description = "Пароль повторно для подтверждения")
    private String confirmPassword;
}
