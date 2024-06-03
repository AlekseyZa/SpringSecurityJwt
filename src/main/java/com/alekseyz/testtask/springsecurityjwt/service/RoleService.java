package com.alekseyz.testtask.springsecurityjwt.service;

import com.alekseyz.testtask.springsecurityjwt.entity.Role;
import com.alekseyz.testtask.springsecurityjwt.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }
}
