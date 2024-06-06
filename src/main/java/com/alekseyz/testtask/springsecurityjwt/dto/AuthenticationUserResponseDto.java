package com.alekseyz.testtask.springsecurityjwt.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Набор данных возвращаемых пользователю после успешной аутентификации и обновления Токенов")
@Builder
@Data
public class AuthenticationUserResponseDto {

    @Schema(description = "Токен доступа")
    String accessToken;
    @Schema(description = "Токен обнолвения")
    String refreshToken;
}
