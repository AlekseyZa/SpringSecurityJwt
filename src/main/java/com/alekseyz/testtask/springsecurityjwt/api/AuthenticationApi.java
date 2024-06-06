package com.alekseyz.testtask.springsecurityjwt.api;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.dto.RefreshTokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1")
@Tag(name = "AuthenticationController", description = "Контроллер для аутентификации и обновления Токена доступа")
public interface AuthenticationApi {

    @Operation(summary = "Аутентификация")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Выполнена аутентификация, возвращает пару Access и Refresh токенов",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    @PostMapping("/authentication")
    AuthenticationUserResponseDto authentication(@RequestBody AuthenticationUserRequestDto authenticationUserRequestDto);


    @Operation(summary = "Обновление токена доступа")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Токен доступа обновлен, возвращает пару Access и Refresh токенов",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    @PostMapping("/refresh-token")
    AuthenticationUserResponseDto refreshToken(@RequestBody RefreshTokenDto request);
}

