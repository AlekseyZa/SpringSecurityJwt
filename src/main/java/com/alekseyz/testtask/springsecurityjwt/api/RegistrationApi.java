package com.alekseyz.testtask.springsecurityjwt.api;

import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/api/v1/registration")
@Tag(name = "RegistrationController", description = "Контроллер для регистрации")
public interface RegistrationApi {

    @Operation(summary = "Регистрация")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Выполнена регистрация в приложении, логин и пароль сохранены в БД",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    @PostMapping("")
    RegistrationUserResponseDto createNewUser(@RequestBody RegistrationUserRequestDto registrationUserRequestDto);
}



