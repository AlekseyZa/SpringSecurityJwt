package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.Token;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import com.alekseyz.testtask.springsecurityjwt.exceptionhandling.InvalidToken;
import com.alekseyz.testtask.springsecurityjwt.repository.TokenRepository;
import com.alekseyz.testtask.springsecurityjwt.service.JwtTokenService;
import com.alekseyz.testtask.springsecurityjwt.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@RequiredArgsConstructor
@Service
public class JwtTokenServiceImplementation implements JwtTokenService {


    private final TokenRepository tokenRepository;
    private final UserService userService;

    @Value("${jwt.access-token-secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh-token-secret}")
    private String refreshTokenSecret;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    @Override
    public String generateAccessToken(User user) {
        return buildToken(new HashMap<>(), user, accessTokenExpiration, accessTokenSecret);
    }

    public String generateRefreshToken(User user) {
        return buildToken(new HashMap<>(), user, refreshTokenExpiration, refreshTokenSecret);
    }

    private String buildToken(Map<String, Object> extraClaims, User user, Long expiration, String tokenSecret) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(tokenSecret))
                .compact();
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
        userName = extractAllClaims(refreshToken).getSubject();
        if (userName == null) {
            throw new InvalidToken("Ошиибка при обновлении токена. Некорректный токен обновления");
        }
        User user = userService.findByUsername(userName).orElseThrow();
        if (!isTokenValid(refreshToken, user)) {
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
    public boolean isTokenValid(String token, User user) {
        String username = extractAllClaims(token).getSubject();
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private Claims extractAllClaims(String token) {
        byte[] keyBytes = Base64.getDecoder().decode(accessTokenSecret.getBytes(StandardCharsets.UTF_8));  //Сделать нормально, убрать хард только на аксес секрет
        SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    @Override
    public String getUsername(String token){
        return  extractAllClaims(token).getSubject();
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

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Key getSigningKey(String tokenSecret) {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
