package com.alekseyz.testtask.springsecurityjwt.service;

import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    ResponseEntity<?> createNewUser(@RequestBody RegistrationUserRequestDto registrationUserDto);
}
