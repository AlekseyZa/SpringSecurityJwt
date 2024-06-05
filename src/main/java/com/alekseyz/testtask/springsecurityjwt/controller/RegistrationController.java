package com.alekseyz.testtask.springsecurityjwt.controller;


import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("")
    public RegistrationUserResponseDto createNewUser(@RequestBody RegistrationUserRequestDto registrationUserRequestDto) {
        return registrationService.registration(registrationUserRequestDto);
    }
}
