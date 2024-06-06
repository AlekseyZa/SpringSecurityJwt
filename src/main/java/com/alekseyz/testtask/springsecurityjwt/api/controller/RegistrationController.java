package com.alekseyz.testtask.springsecurityjwt.api.controller;


import com.alekseyz.testtask.springsecurityjwt.api.RegistrationApi;
import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController implements RegistrationApi {

    private final RegistrationService registrationService;

    @Override
    public RegistrationUserResponseDto createNewUser(@RequestBody RegistrationUserRequestDto registrationUserRequestDto) {
        return registrationService.registration(registrationUserRequestDto);
    }
}
