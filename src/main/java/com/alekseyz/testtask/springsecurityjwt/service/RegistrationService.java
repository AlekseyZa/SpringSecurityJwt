package com.alekseyz.testtask.springsecurityjwt.service;

import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserResponseDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface RegistrationService {
    RegistrationUserResponseDto registration(@RequestBody RegistrationUserRequestDto registrationUserRequestDto);
}
