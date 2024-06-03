package com.alekseyz.testtask.springsecurityjwt.controller;


import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.service.Implementation.AuthenticationServiceImplementation;
import com.alekseyz.testtask.springsecurityjwt.service.Implementation.UserServiceImplementation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationServiceImplementation authenticationServiceImplementation;
    private final UserServiceImplementation userServiceImplementation;


    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationUserResponseDto> createAuthenticationToken(
            @RequestBody AuthenticationUserRequestDto authenticationUserRequestDto) {
        return ResponseEntity.ok(authenticationServiceImplementation.createAuthToken(authenticationUserRequestDto));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        authenticationServiceImplementation.refreshToken(request, response);
    }
}
