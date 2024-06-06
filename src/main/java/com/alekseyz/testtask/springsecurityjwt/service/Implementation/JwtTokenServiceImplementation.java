package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.Token;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import com.alekseyz.testtask.springsecurityjwt.exceptionhandling.InvalidTokenException;
import com.alekseyz.testtask.springsecurityjwt.repository.TokenRepository;
import com.alekseyz.testtask.springsecurityjwt.service.JwtTokenService;
import com.alekseyz.testtask.springsecurityjwt.service.JwtUtilService;
import com.alekseyz.testtask.springsecurityjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JwtTokenServiceImplementation implements JwtTokenService {

    @Value("${jwt.access-token-secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh-token-secret}")
    private String refreshTokenSecret;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final JwtUtilService jwtUtilService;

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return jwtUtilService.buildAccessToken(new HashMap<>(), userDetails, accessTokenSecret, accessTokenExpiration);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return jwtUtilService.buildRefreshToken(userDetails, refreshTokenSecret, refreshTokenExpiration);
    }

    @Override
    public AuthenticationUserResponseDto refreshToken(String refreshToken) {
        if (!jwtUtilService.isTokenValid(refreshToken, refreshTokenSecret)) {
            throw new InvalidTokenException("Некорректный токен обновления");
        }
        String userName = jwtUtilService.extractAllClaims(refreshToken, refreshTokenSecret).getSubject();
        User user = userService.findByUsername(userName).orElseThrow();
        List<Token> tokenList = tokenRepository.findTokensByUserAndExpiredFalseAndRevokedFalse(user)
                .orElseThrow();
        if (tokenList.isEmpty()) {
            throw new InvalidTokenException("Ошиибка при обновлении токена. Некорректный токен обновления");
        }
        String refreshTokenFromDB = tokenList.get(0).getToken();
        if (!(refreshTokenFromDB != null && refreshTokenFromDB.equals(refreshToken))) {
            throw new InvalidTokenException("Ошиибка при обновлении токена. Некорректный токен обновления");
        }
        String accessToken = generateAccessToken(user);
        return AuthenticationUserResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void lockAnotherValidUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findTokensByUserAndExpiredFalseAndRevokedFalse(user).orElseThrow();
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
    public boolean isAccessTokenValid(String jwtAccessToken) {
        return jwtUtilService.isTokenValid(jwtAccessToken, accessTokenSecret);
    }

}
