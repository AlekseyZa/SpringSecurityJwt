package com.alekseyz.testtask.springsecurityjwt.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtUtilService {

    String buildToken(Map<String, Object> extraClaims, UserDetails userDetails,
                      String tokenSecret, Long tokenExpiration);

    boolean isTokenValid(String token, UserDetails userDetails, String tokenSecret);

    String getUsername(String token, String tokenSecret);

    boolean isTokenExpired(String token, String tokenSecret);

    Claims extractAllClaims(String token, String tokenSecret);
}
