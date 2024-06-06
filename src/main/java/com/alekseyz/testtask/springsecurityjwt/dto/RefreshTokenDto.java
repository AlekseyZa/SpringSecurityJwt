package com.alekseyz.testtask.springsecurityjwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Набор данных отправляемых при обновлении токена доступа")
@Data
public class RefreshTokenDto {
    @Schema(description = "Токен обновления")
    String refreshToken;
}
