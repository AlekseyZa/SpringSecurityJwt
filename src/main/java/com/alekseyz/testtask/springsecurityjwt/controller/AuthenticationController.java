package com.alekseyz.testtask.springsecurityjwt.controller;


import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authentication")
    public AuthenticationUserResponseDto authentication(
            @RequestBody AuthenticationUserRequestDto authenticationUserRequestDto) {
        return authenticationService.authentication(authenticationUserRequestDto);
    }

    @PostMapping("/refresh-token")
    public AuthenticationUserResponseDto refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        return authenticationService.refreshToken(request, response);
    }
}
