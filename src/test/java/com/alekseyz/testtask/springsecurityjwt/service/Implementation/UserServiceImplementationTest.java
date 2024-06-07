package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.entity.Role;
import com.alekseyz.testtask.springsecurityjwt.entity.Token;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import com.alekseyz.testtask.springsecurityjwt.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImplementation userServiceImplementation;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Тестирование на возврат пользователя по имени")
    void findByUsernameTest() {
        String username = "user";
        User user = new User(1L,"user",null,null,null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> newUser = userServiceImplementation.findByUsername(username);

        Assertions.assertEquals(newUser,Optional.of(user));
    }

    @Test
    @DisplayName("Тестирование на возврат Userdetails по имени пользователя")
    void loadUserByUsernameTest() {
        List<Role> listRole = new ArrayList<>();
        List<Token> listToken = new ArrayList<>();
        String username = "user";
        UserDetails user = new User(1L,"user","password",listRole,listToken);
        org.springframework.security.core.userdetails.User simpleUser = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities());
        User newUser = new User(1L,"user","password",listRole,listToken);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(newUser));

        UserDetails userDetails = userServiceImplementation.loadUserByUsername(username);

        Assertions.assertEquals(userDetails,simpleUser);
    }

    @Test
    @DisplayName("Тестирование сохранения пользователя")
    void saveTest() {
        User user = new User(1L,"user",null,null,null);

         when(userRepository.save(user)).thenReturn(user);

        User savedUser = userServiceImplementation.save(user);

        Assertions.assertEquals(savedUser,user);

    }

}