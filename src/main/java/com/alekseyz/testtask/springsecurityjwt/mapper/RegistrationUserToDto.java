package com.alekseyz.testtask.springsecurityjwt.mapper;

import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RegistrationUserToDto implements Mapper<User, RegistrationUserResponseDto> {

    @Override
    public RegistrationUserResponseDto map(User user) {
        return RegistrationUserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();

    }

}
