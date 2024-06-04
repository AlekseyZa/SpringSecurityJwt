package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserRequestDto;
import com.alekseyz.testtask.springsecurityjwt.dto.RegistrationUserResponseDto;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import com.alekseyz.testtask.springsecurityjwt.repository.UserRepository;
import com.alekseyz.testtask.springsecurityjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final RoleServiceImplementation roleServiceImplementation;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserRequestDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            System.out.println(registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword()));
            //return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new IllegalStateException("Пароли не совпадают"), HttpStatus.BAD_REQUEST);
        }
        if (findByUsername(registrationUserDto.getUsername()).isPresent()) {
            //return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с указанным именем уже существует"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new IllegalStateException("Пользователь с таким именем уже существует, укажите другой логин"), HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(registrationUserDto.getPassword()));
        user.setRoles(List.of(roleServiceImplementation.getUserRole()));
        userRepository.save(user);                     //Сделать красивее и вернуть в респонсе уже сохранненый юзер
        return ResponseEntity.ok(new RegistrationUserResponseDto(user.getId(), user.getUsername()));
    }
}
