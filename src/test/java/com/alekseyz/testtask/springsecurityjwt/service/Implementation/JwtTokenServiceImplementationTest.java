package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.Role;
import com.alekseyz.testtask.springsecurityjwt.entity.Token;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import com.alekseyz.testtask.springsecurityjwt.repository.TokenRepository;
import com.alekseyz.testtask.springsecurityjwt.service.JwtUtilService;
import com.alekseyz.testtask.springsecurityjwt.service.UserService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceImplementationTest {

    private final String accessTokenSecret = "aklgerggreg";

    private final String refreshTokenSecret = "lknfvwenojiwe";

    private final Long accessTokenExpiration = 10000L;

    private final Long refreshTokenExpiration = 50000L;

    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserService userService;
    @Mock
    private JwtUtilService jwtUtilService;
    @InjectMocks
    private JwtTokenServiceImplementation jwtTokenServiceImplementation;



    @BeforeEach
    void setUp() {
    }

    @Test
    void generateAccessToken() {
    }

    @Test
    void generateRefreshToken() {
    }

    @Disabled
    @Test
    @DisplayName("Проверка обновления токена доступа с помощью токена обновления")
    void refreshToken_returnUpdatedAccessToken() {
        String refreshToken = "gjhqe5jw4j";
        String updatedAccessToken = "1y34jhrjkfdjhasgaergfwe";
        Token token = new Token(1L, "gjhqe5jw4j", null,false,false,null);
        List<Token> tokenList = new ArrayList<>();
        tokenList.add(token);
        List<Role> roles = new ArrayList<>();
        User user = new User(1L, "user", "password", roles,tokenList);
        Claims claims = Mockito.mock(Claims.class);

        AuthenticationUserResponseDto responseDto =
                AuthenticationUserResponseDto.builder()
                        .accessToken(updatedAccessToken)
                        .refreshToken(refreshToken)
                        .build();

        when(jwtUtilService.extractAllClaims(refreshToken, refreshTokenSecret)).thenReturn(claims);
        when(jwtUtilService.isTokenValid(refreshToken, refreshTokenSecret)).thenReturn(true);
        when(userService.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(tokenRepository.findTokensByUserAndExpiredFalseAndRevokedFalse(user)).thenReturn(Optional.of(tokenList));

        AuthenticationUserResponseDto expectedResponseDto = jwtTokenServiceImplementation.refreshToken(refreshToken);

        Assertions.assertNotNull(expectedResponseDto.getAccessToken());
        Assertions.assertNotNull(expectedResponseDto.getRefreshToken());
        Assertions.assertEquals(expectedResponseDto, responseDto);
    }

    @Test
    @DisplayName("Проверка деактивации ненужных токенов")
    void lockAnotherValidUserTokens() {
        Token token = new Token(1L, "gjhqe5jw4j", null,false,false,null);
        List<Token> tokenList = new ArrayList<>();
        tokenList.add(token);
        List<Role> roles = new ArrayList<>();
        User user = new User(1L, "user", "password", roles,tokenList);

        when(tokenRepository.findTokensByUserAndExpiredFalseAndRevokedFalse(user)).thenReturn(Optional.of(tokenList));

        jwtTokenServiceImplementation.lockAnotherValidUserTokens(user);

        Mockito.verify(tokenRepository,Mockito.atLeastOnce()).findTokensByUserAndExpiredFalseAndRevokedFalse(user);
        Mockito.verify(tokenRepository,Mockito.atLeastOnce()).saveAll(tokenList);
    }

    @Disabled
    @Test
    @DisplayName("Проверка сохранения токена")
    void saveToken() {
        String refreshToken = "gjhqe5jw4j";
        String type = "refresh";
        List<Token> tokenList = new ArrayList<>();
        List<Role> roles = new ArrayList<>();
        User user = new User(1L, "user", "password", roles,tokenList);
        Token token = new Token(1L, "gjhqe5jw4j", null,false,false,null);
        tokenList.add(token);

        when(tokenRepository.save(token)).thenReturn(token);

        jwtTokenServiceImplementation.saveToken(user,refreshToken,type);

        Assertions.assertEquals(tokenRepository.save(token),token);
        Mockito.verify(tokenRepository,Mockito.atLeastOnce()).save(token);



    }

    @Test
    void getUsername() {
    }

    @Test
    void isAccessTokenValid() {
    }
}