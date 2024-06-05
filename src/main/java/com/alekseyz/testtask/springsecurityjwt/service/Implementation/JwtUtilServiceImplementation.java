package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.service.JwtUtilService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;


@Service
public class JwtUtilServiceImplementation implements JwtUtilService {

    @Override
    public String buildAccessToken(Map<String, Object> extraClaims, UserDetails userDetails,
                             String tokenSecret, Long tokenExpiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSigningKey(tokenSecret))
                .compact();
    }

    @Override
    public String buildRefreshToken(UserDetails userDetails,
                             String tokenSecret, Long tokenExpiration) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSigningKey(tokenSecret))
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, String tokenSecret) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey(tokenSecret))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getUsername(String token, String tokenSecret) {
        return extractAllClaims(token, tokenSecret).getSubject();
    }

    @Override
    public Claims extractAllClaims(String token, String tokenSecret) {
        return Jwts.parser().verifyWith(getSigningKey(tokenSecret)).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSigningKey(String tokenSecret) {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
