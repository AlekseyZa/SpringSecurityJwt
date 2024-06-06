package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.exceptionhandling.UserException;
import com.alekseyz.testtask.springsecurityjwt.mapper.RegistrationDtoToUser;
import com.alekseyz.testtask.springsecurityjwt.mapper.RegistrationUserToDto;
import com.alekseyz.testtask.springsecurityjwt.service.RegistrationService;
import com.alekseyz.testtask.springsecurityjwt.service.RoleService;
import com.alekseyz.testtask.springsecurityjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegistrationServiceImplementation implements RegistrationService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RegistrationDtoToUser registrationDtoToUser;
    private final RegistrationUserToDto registrationUserToDto;
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public RegistrationUserResponseDto registration(@RequestBody RegistrationUserRequestDto registrationUserRequestDto) {
        if (!registrationUserRequestDto.getPassword().equals(registrationUserRequestDto.getConfirmPassword())) {
            throw new UserException("Пароли не совпадают");
        }
        if (userService.findByUsername(registrationUserRequestDto.getUsername()).isPresent()) {
            throw new UserException("Пользователь с таким именем уже существует, укажите другой логин");
        }
        return Optional.of(registrationUserRequestDto)
                .map(registrationDtoToUser::map)
                .map(user1 -> {
                    user1.setPassword(encodePassword(registrationUserRequestDto.getPassword()));
                    user1.setRoles(roleService.getStandartUserRole());
                    return user1;
                })
                .map(userService::save)
                .map(registrationUserToDto::map)
                .orElseThrow(() ->
                        new UserException("Ошибка при записи пользователя в БД"));
    }

    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}
