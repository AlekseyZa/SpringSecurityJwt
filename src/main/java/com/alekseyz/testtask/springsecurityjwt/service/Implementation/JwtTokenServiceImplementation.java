package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.dto.AuthenticationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.Token;
import com.alekseyz.testtask.springsecurityjwt.entity.TokenType;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import com.alekseyz.testtask.springsecurityjwt.repository.TokenRepository;
import com.alekseyz.testtask.springsecurityjwt.repository.UserRepository;
import com.alekseyz.testtask.springsecurityjwt.service.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtTokenServiceImplementation implements JwtTokenService {


    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

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
        return generateAccessToken(new HashMap<>(), user);
    }

    private String buildToken(Map<String, Object> extraClaims, User user, Long expiration, String tokenSecret) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(tokenSecret), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        return buildToken(new HashMap<>(), user, refreshTokenExpiration, refreshTokenSecret);
    }

    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userName;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userName = extractUsername(refreshToken);
        if (userName != null) {
            User user = userRepository.findByUsername(userName)
                    .orElseThrow();  //сделать нормальную обработку
            if (isTokenValid(refreshToken, user)) {
                String accessToken = generateRefreshToken(user);
                lockAnotherValidUserTokens(user);
                saveToken(user, accessToken);
                AuthenticationUserResponseDto authenticationUserResponseDto = AuthenticationUserResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authenticationUserResponseDto);
            }
        }
    }

    @Override
    public String generateToken(User user) {
        return null;
    }

    @Override
    public String generateAccessToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user, accessTokenExpiration, accessTokenSecret);
    }

    @Override//проверяет валидность токена
    public boolean isTokenValid(String token, User user) {
        String username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    @Override  //По токену достает юзера
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        byte[] keyBytes = Base64.getDecoder().decode(accessTokenSecret.getBytes(StandardCharsets.UTF_8));  //Сделать нормально, убрать хард только на аксес секрет
        SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA256");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ExtractClaim");//Проверить, а то рандомно составил
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();


//                .parserBuilder()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//
//        Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload();
//                Jwts.parser()
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();



//        Jwts.
//                parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(key))
//                .build().parseClaimsJws(token);
    }

    //Блокируем другие живые токены
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

    //сохранение токена
    @Override
    public void saveToken(User user, String accesToken) {
        Token token = Token.builder()
                .user(user)
                .token(accesToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSigningKey(String tokenSecret) {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
