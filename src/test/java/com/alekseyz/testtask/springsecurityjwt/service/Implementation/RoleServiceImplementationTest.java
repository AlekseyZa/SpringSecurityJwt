package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.entity.Role;
import com.alekseyz.testtask.springsecurityjwt.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplementationTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    RoleServiceImplementation roleServiceImplementation;

    @Test
    @DisplayName("Тестирование на возврат стандартной роли пользователя")
    void getStandartUserRole() {
        Role role = new Role();
        role.setId(1L);
        role.setName("USER");

        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));

        List<Role> listRole = roleServiceImplementation.getStandartUserRole();

        Assertions.assertEquals(listRole.get(0),role);

    }


}