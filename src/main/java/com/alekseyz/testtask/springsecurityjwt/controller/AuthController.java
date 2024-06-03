package com.alekseyz.testtask.springsecurityjwt.controller;


import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserDto;
import com.alekseyz.testtask.springsecurityjwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

//    @PostMapping("/auth")
//    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
//        return authService.createAuthToken(authRequest);
//    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }
}
