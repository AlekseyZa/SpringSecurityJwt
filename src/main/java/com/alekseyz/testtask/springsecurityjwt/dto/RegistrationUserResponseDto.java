package com.alekseyz.testtask.springsecurityjwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Набор данных возвращаемый после успешной регистрации")
@Builder
@Data
@AllArgsConstructor
public class RegistrationUserResponseDto {
    @Schema(description = "ID пользователя")
    private Long id;
    @Schema(description = "Логин")
    private String username;
}
