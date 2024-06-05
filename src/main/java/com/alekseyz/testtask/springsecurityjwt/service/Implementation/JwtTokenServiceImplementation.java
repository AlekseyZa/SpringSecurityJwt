package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.Token;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import com.alekseyz.testtask.springsecurityjwt.exceptionhandling.InvalidToken;
import com.alekseyz.testtask.springsecurityjwt.repository.TokenRepository;
import com.alekseyz.testtask.springsecurityjwt.service.JwtTokenService;
import com.alekseyz.testtask.springsecurityjwt.service.JwtUtilService;
import com.alekseyz.testtask.springsecurityjwt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.*;

@RequiredArgsConstructor
@Service
public class JwtTokenServiceImplementation implements JwtTokenService {

    @Value("${jwt.access-token-secret}")
    private String accessTokenSecret;

//    @Value("${jwt.refresh-token-secret}")
//    private String refreshTokenSecret;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final JwtUtilService jwtUtilService;

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return jwtUtilService.buildToken(new HashMap<>(), userDetails, accessTokenSecret, accessTokenExpiration);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return jwtUtilService.buildToken(new HashMap<>(), userDetails, accessTokenSecret, refreshTokenExpiration);
    }

    @Override
    public AuthenticationUserResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userName;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidToken("Ошиибка при обновлении токена. Не предоставлен токен обновления");
        }
        refreshToken = authHeader.substring(7);
        userName = jwtUtilService.extractAllClaims(refreshToken, accessTokenSecret).getSubject();
        if (userName == null) {
            throw new InvalidToken("Ошиибка при обновлении токена. Некорректный токен обновления");
        }
        User user = userService.findByUsername(userName).orElseThrow();
        if (!jwtUtilService.isTokenValid(refreshToken, user, accessTokenSecret)) {
            throw new InvalidToken("Ошиибка при обновлении токена. Некорректный токен обновления");
        }
        String accessToken = generateAccessToken(user);
        lockAnotherValidUserTokens(user);
        saveToken(user, accessToken, "Access");
        return AuthenticationUserResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void lockAnotherValidUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findTokensByUserAndExpiredFalseAndRevokedFalse(user);
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void saveToken(User user, String token, String type) {
        Token tokenEntity = Token.builder()
                .user(user)
                .token(token)
                .tokenType(type)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(tokenEntity);
    }

    @Override
    public String getUsername(String jwtAccessToken) {
        return jwtUtilService.getUsername(jwtAccessToken, accessTokenSecret);
    }

    @Override
    public boolean isTokenValid(String jwtAccessToken, UserDetails userDetails) {
        return jwtUtilService.isTokenValid(jwtAccessToken, userDetails, accessTokenSecret);
    }

}
