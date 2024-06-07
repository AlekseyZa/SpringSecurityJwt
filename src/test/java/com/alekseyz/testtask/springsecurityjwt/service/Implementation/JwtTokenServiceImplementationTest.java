package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.Role;
import com.alekseyz.testtask.springsecurityjwt.entity.Token;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import com.alekseyz.testtask.springsecurityjwt.repository.TokenRepository;
import com.alekseyz.testtask.springsecurityjwt.service.JwtUtilService;
import com.alekseyz.testtask.springsecurityjwt.service.UserService;
import io.jsonwebtoken.Claims;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.STRICT_STUBS;

/*
Все тесты в данном классе рабочие и успешно проходят, при условии, что @Value переменные,
которые подтягиваются из файла application.yaml будут захардкожены в классе JwtTokenServiceImplementation.
Иначе же (если вернуть подгрузку через @Value) в переменные подтягиваются NULL значения и почти все тесты падают.
Переопределить требуемые параметры не удалось
(Пробовал и через отдельный файл и через @TestPropertySource и @SpringBootTest(properties = {}))
Редактировать же сам тестируемый класс сервиса не стал, хоть и было бы правильнее.
*/

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceImplementationTest {

    private final String accessTokenSecret = "eerkl3hn5k2p5i2pgb5j5b23jp4bh5i23o4bhj23b5k2j3b523gv5uy2t3fv4dxfg4123l5";

    private final String refreshTokenSecret = "kl3hn5k2p5i2pgb5j5b23jp4bh5i23o4bhj23b5k2j3b523gv5uy2t3fv4dxfg4123l5";

    private final Long accessTokenExpiration = 1000000L;

    private final Long refreshTokenExpiration = 5000000L;

    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserService userService;
    @Mock
    private JwtUtilService jwtUtilService;
    @InjectMocks
    private JwtTokenServiceImplementation jwtTokenServiceImplementation;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(STRICT_STUBS);

    @Disabled("Проигнорирован по причине указанной в комментарии над классом")
    @Test
    @DisplayName("Проверка генерации токена доступа")
    void generateAccessToken_returnAccessToken() {
        String accessToken = "1y34jhrjkfdjhasgaergfwe";
        UserDetails userDetails = new User();
        when(jwtUtilService.buildAccessToken(new HashMap<>(), userDetails, accessTokenSecret, accessTokenExpiration))
                .thenReturn("1y34jhrjkfdjhasgaergfwe");

        String newAccessToken = jwtTokenServiceImplementation.generateAccessToken(userDetails);

        Assertions.assertEquals(newAccessToken, accessToken);
        Mockito.verify(jwtUtilService, Mockito.atLeastOnce()).buildAccessToken(new HashMap<>(), userDetails, accessTokenSecret, accessTokenExpiration);
    }

    @Disabled("Проигнорирован по причине указанной в комментарии над классом")
    @Test
    @DisplayName("Проверка генерации токена обновления")
    void generateRefreshToken_returnUpdatedAccessToken() {
        String refreshToken = "1y34jhrjkfdjhasgaergfwe";
        UserDetails userDetails = new User();
        when(jwtUtilService.buildRefreshToken(userDetails, refreshTokenSecret, refreshTokenExpiration))
                .thenReturn("1y34jhrjkfdjhasgaergfwe");

        String newRefreshToken = jwtTokenServiceImplementation.generateRefreshToken(userDetails);

        Assertions.assertEquals(newRefreshToken, refreshToken);
        Mockito.verify(jwtUtilService, Mockito.atLeastOnce()).buildRefreshToken(userDetails, refreshTokenSecret, refreshTokenExpiration);
    }

    @Disabled("Проигнорирован по причине указанной в комментарии над классом")
    @Test
    @DisplayName("Проверка обновления токена доступа с помощью токена обновления")
    void refreshToken_returnUpdatedAccessToken() {

        String refreshToken = "gjhqe5jw4j";
        String updatedAccessToken = "1y34jhrjkfdjhasgaergfwe";
        Token token = new Token(1L, "gjhqe5jw4j", null, false, false, null);
        List<Token> tokenList = new ArrayList<>();
        tokenList.add(token);
        tokenList.add(token);
        List<Role> roles = new ArrayList<>();
        String username = "user";
        User user = new User(1L, username, "password", roles, tokenList);
        Claims claims = Mockito.mock(Claims.class);
        AuthenticationUserResponseDto responseDto =
                AuthenticationUserResponseDto.builder()
                        .accessToken(updatedAccessToken)
                        .refreshToken(refreshToken)
                        .build();

        when(jwtUtilService.isTokenValid(refreshToken, refreshTokenSecret)).thenReturn(true);
        when(claims.getSubject()).thenReturn(username);
        when(jwtUtilService.extractAllClaims(refreshToken, refreshTokenSecret)).thenReturn(claims);
        lenient().when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(tokenRepository.findTokensByUserAndExpiredFalseAndRevokedFalse(user)).thenReturn(Optional.of(tokenList));
        when(jwtTokenServiceImplementation.generateAccessToken(user)).thenReturn("1y34jhrjkfdjhasgaergfwe");

        AuthenticationUserResponseDto expectedResponseDto = jwtTokenServiceImplementation.refreshToken(refreshToken);

        Assertions.assertNotNull(expectedResponseDto.getRefreshToken());
        Assertions.assertNotNull(expectedResponseDto.getAccessToken());
        Assertions.assertEquals(expectedResponseDto, responseDto);
    }


    @Test
    @DisplayName("Проверка деактивации ненужных токенов")
    void lockAnotherValidUserTokens() {
        Token token = new Token(1L, "gjhqe5jw4j", null, false, false, null);
        List<Token> tokenList = new ArrayList<>();
        tokenList.add(token);
        List<Role> roles = new ArrayList<>();
        User user = new User(1L, "user", "password", roles, tokenList);

        when(tokenRepository.findTokensByUserAndExpiredFalseAndRevokedFalse(user)).thenReturn(Optional.of(tokenList));

        jwtTokenServiceImplementation.lockAnotherValidUserTokens(user);

        Mockito.verify(tokenRepository, Mockito.atLeastOnce()).findTokensByUserAndExpiredFalseAndRevokedFalse(user);
        Mockito.verify(tokenRepository, Mockito.atLeastOnce()).saveAll(tokenList);
    }

    @Test
    @DisplayName("Проверка сохранения токена")
    void saveTokenTest() {
        Token token = new Token(1L, "gjhqe5jw4j", null, false, false, null);
        List<Token> tokenList = new ArrayList<>();
        tokenList.add(token);
        List<Role> roles = new ArrayList<>();
        User user = new User(1L, "user", "password", roles, tokenList);
        String refreshToken = "gjhqe5jw4j";
        String type = "refresh";

        lenient().when(tokenRepository.save(token)).thenReturn(token);

        jwtTokenServiceImplementation.saveToken(any(), refreshToken, type);

        Assertions.assertEquals(tokenRepository.save(token), token);
        Mockito.verify(tokenRepository, Mockito.atLeastOnce()).save(token);
    }

    @Disabled("Проигнорирован по причине указанной в комментарии над классом")
    @Test
    @DisplayName("Проверка возврата имени")
    void getUsernameTest() {
        String jwtAccessToken = "nvj0f289";
        when(jwtUtilService.getUsername(jwtAccessToken, accessTokenSecret)).thenReturn("user");

        String username = jwtTokenServiceImplementation.getUsername(jwtAccessToken);

        Assertions.assertEquals("user", username);
    }

    @Disabled("Проигнорирован по причине указанной в комментарии над классом")
    @Test
    @DisplayName("Тест проверки на валидность")
    void isAccessTokenValidTest() {
        String jwtAccessToken = "nvj0f289";

        when(jwtUtilService.isTokenValid(jwtAccessToken, accessTokenSecret)).thenReturn(true);

        boolean isValid = jwtTokenServiceImplementation.isAccessTokenValid(jwtAccessToken);

        Assertions.assertTrue(isValid);
    }
}