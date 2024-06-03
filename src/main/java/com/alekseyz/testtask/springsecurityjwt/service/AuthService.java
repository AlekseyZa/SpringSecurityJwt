package com.alekseyz.testtask.springsecurityjwt.service;

import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserDto;
import com.alekseyz.testtask.springsecurityjwt.dto.UserDto;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
  //  private final JwtTokenUtil jwtTokenUtil;
   // private final AuthenticationManager authenticationManager;

//    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//        } catch (BadCredentialsException e) {
//            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
//        }
//        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
//        String token = jwtTokenUtil.generateToken(userDetails);
//        return ResponseEntity.ok(new JwtResponse(token));
//    }

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            System.out.println(registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword()));
            //return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new IllegalStateException("Пароли не совпадают"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            //return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с указанным именем уже существует"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new IllegalStateException("Пользователь с таким именем уже существует, укажите другой логин"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername()));
    }
}
