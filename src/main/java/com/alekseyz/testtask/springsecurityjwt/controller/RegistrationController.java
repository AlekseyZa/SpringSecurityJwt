package com.alekseyz.testtask.springsecurityjwt.controller;


import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.service.Implementation.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration")
public class RegistrationController {

    private final UserServiceImplementation userServiceImplementation;

    @PostMapping("")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserRequestDto registrationUserRequestDto) {
        return ResponseEntity.ok(userServiceImplementation.createNewUser(registrationUserRequestDto));
    }
}
