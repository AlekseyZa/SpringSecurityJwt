package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtUtilServiceImplementationTest {

    private final String tokenSecret = "eerkl3hn5k2p5i2pgb5j5b23jp4bh5i23o4bhj23b5k2j3b523gv5uy2t3fv4dxfg4123l5";
    private final Long tokenExpiration = 1000000L;
    JwtUtilServiceImplementation jwtUtilServiceImplementation;

    @BeforeEach
    void setUp() {
        jwtUtilServiceImplementation = new JwtUtilServiceImplementation();
    }

    @Test
    @DisplayName("Проверка генерации токена доступа")
    void buildAccessTokenTest() {
        Map<String, Object> extraClaims = new HashMap<>();
        UserDetails userDetails = new User();

        String token = jwtUtilServiceImplementation.buildAccessToken(extraClaims, userDetails,tokenSecret, tokenExpiration);

        Assertions.assertNotNull(token);
    }

    @Test
    @DisplayName("Проверка генерации токена обновления")
    void buildRefreshTokenTest() {
        UserDetails userDetails = new User();

        String token = jwtUtilServiceImplementation.buildRefreshToken(userDetails,tokenSecret, tokenExpiration);

        Assertions.assertNotNull(token);

    }

    @Test
    @DisplayName("Проверка валидации токена")
    void isTokenValidTest() {
        UserDetails userDetails = new User();
       String token = jwtUtilServiceImplementation.buildRefreshToken(userDetails,tokenSecret,tokenExpiration);

        boolean isValid = jwtUtilServiceImplementation.isTokenValid(token,tokenSecret);

        Assertions.assertTrue(isValid);
    }

    @Test
    @DisplayName("Проверка получения имени пользователя")
    void getUsernameTest() {
        UserDetails userDetails = new User(1L, "user", null, null, null);
        String token = jwtUtilServiceImplementation.buildRefreshToken(userDetails,tokenSecret,tokenExpiration);

        String username = jwtUtilServiceImplementation.getUsername(token,tokenSecret);

        Assertions.assertEquals(username,"user");
    }

    @Test@DisplayName("Проверка выгрузки утверждений из токена")
    void extractAllClaimsTest() {
        UserDetails userDetails = new User(1L, "user", null, null, null);
        String token = jwtUtilServiceImplementation.buildRefreshToken(userDetails,tokenSecret,tokenExpiration);

        Claims claims = jwtUtilServiceImplementation.extractAllClaims(token, tokenSecret);

        assertEquals(userDetails.getUsername(), claims.getSubject());
    }

}