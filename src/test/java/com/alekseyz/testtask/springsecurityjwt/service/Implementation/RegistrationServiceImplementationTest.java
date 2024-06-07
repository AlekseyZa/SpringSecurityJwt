package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import com.alekseyz.testtask.springsecurityjwt.mapper.RegistrationDtoToUser;
import com.alekseyz.testtask.springsecurityjwt.mapper.RegistrationUserToDto;
import com.alekseyz.testtask.springsecurityjwt.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplementationTest {

    @Mock
    private RegistrationDtoToUser registrationDtoToUser;
    @Mock
    private RegistrationUserToDto registrationUserToDto;
    @Mock
    private UserService userService;
    @InjectMocks
    RegistrationServiceImplementation registrationService;

    @Test
    @DisplayName("Проверка регистрации")
    void registrationTest() {
        RegistrationUserRequestDto registrationUserRequestDto = RegistrationUserRequestDto.builder()
                .username("user")
                .password("password")
                .confirmPassword("password")
                .build();
        RegistrationUserResponseDto registrationUserResponseDto = RegistrationUserResponseDto.builder()
                .id(1L)
                .username("user")
                .build();
        User user = new User(1L, "user", "password", null, null);
        when(userService.findByUsername("user")).thenReturn(Optional.empty());
        when(registrationDtoToUser.map(registrationUserRequestDto)).thenReturn(user);
        when(userService.save(user)).thenReturn(user);
        when(registrationUserToDto.map(user)).thenReturn(registrationUserResponseDto);

        RegistrationUserResponseDto newRegistrationUserResponseDto = registrationService.registration(registrationUserRequestDto);

        Assertions.assertEquals(registrationUserResponseDto,newRegistrationUserResponseDto);
    }




}