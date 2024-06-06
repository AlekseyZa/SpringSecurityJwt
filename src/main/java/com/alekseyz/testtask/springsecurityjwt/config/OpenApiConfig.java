package com.alekseyz.testtask.springsecurityjwt.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "SpringSecurityJwt application",
                description = "Приложение с функционалом аутентификации/авторизации пользователей с использованием JWT"
                , version = "1.0.0",
                contact = @Contact(
                        name = "Алексей Заборников"
                )
        ), security = {@SecurityRequirement(name = "Bearer Token")}
)


@SecurityScheme(
        name = "Bearer Token",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")
public class OpenApiConfig {

}
