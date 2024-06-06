package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.Role;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import com.alekseyz.testtask.springsecurityjwt.service.JwtTokenService;
import com.alekseyz.testtask.springsecurityjwt.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplementationTest {

    @Mock
    private UserService userService;
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    AuthenticationServiceImplementation authenticationServiceImplementation;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Тестирование аутентификации через логин/пароль")
    void authentication_returnJwtTokens() {
        AuthenticationUserRequestDto request =
                AuthenticationUserRequestDto.builder()
                        .username("user")
                        .password("password")
                        .build();
        List<Role> roles = new ArrayList<>();
        User user = new User(1L, "user", "password", roles,null);

        when(userService.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
        when(jwtTokenService.generateRefreshToken(user)).thenReturn("fwefwefwefw");
        when(jwtTokenService.generateAccessToken(user)).thenReturn("4jh4h45grf3");

        AuthenticationUserResponseDto responseDto = authenticationServiceImplementation.authentication(request);

        Mockito.verify(authenticationManager,Mockito.atLeastOnce()).authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        Assertions.assertNotNull(responseDto.getAccessToken());
        Assertions.assertNotNull(responseDto.getRefreshToken());
    }

//    @Test
//    @DisplayName("Тестирование обновления токена доступа с помощью токена обновления")
//    void refreshAccessToken_returnJwtTokens() {
//        String refreshToken = "gjhqe5jw4j";
//        String updatedAccessToken = "1y34jhrjkfdjhasgaergfwe";
//        AuthenticationUserResponseDto responseDto =
//                AuthenticationUserResponseDto.builder()
//                        .accessToken(updatedAccessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//
//        when(jwtTokenService.refreshToken(refreshToken)).thenReturn(responseDto);
//
//        AuthenticationUserResponseDto expectedResponseDto = authenticationServiceImplementation.refreshToken(refreshToken);
//
//        Mockito.verify(jwtTokenService,Mockito.atLeastOnce()).refreshToken(refreshToken);
//        Assertions.assertEquals(expectedResponseDto, responseDto);
//    }

}