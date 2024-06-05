package com.alekseyz.testtask.springsecurityjwt.mapper;

import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RegistrationDtoToUser implements Mapper<RegistrationUserRequestDto, User> {
    @Override
    public User map(RegistrationUserRequestDto registrationUserRequestDto) {
        User user = new User();
        user.setUsername(registrationUserRequestDto.getUsername());
        user.setPassword(registrationUserRequestDto.getPassword());
        return user;
    }
}
