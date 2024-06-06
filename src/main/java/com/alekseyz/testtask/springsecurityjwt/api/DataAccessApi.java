package com.alekseyz.testtask.springsecurityjwt.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/api/v1")
@Tag(name = "DataAccessController", description = "Контроллер для проверки доступа к данным в зависимости от прав")
public interface DataAccessApi {
    @Operation(summary = "Ресурс администратора")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получен доступ к ресурсам пользователя с правами ADMIN",
                    content = @Content(
                            mediaType = "text/html;charset=utf-8"
                    ))
    })
    @GetMapping("/admin")
    String adminData();

    @Operation(summary = "Ресурс пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получен доступ к ресурсам пользователя с правами USER",
                    content = @Content(
                            mediaType = "text/html;charset=utf-8"
                    ))
    })
    @GetMapping("/user")
    String userData();

    @Operation(summary = "Общедоступный ресурс")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получен доступ к общедоступному ресурсу",
                    content = @Content(
                            mediaType = "text/html;charset=utf-8"
                    ))
    })
    @GetMapping("/public")
    String publicData();

}



