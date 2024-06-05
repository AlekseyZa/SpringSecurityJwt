package com.alekseyz.testtask.springsecurityjwt.service.Implementation;

import com.alekseyz.testtask.springsecurityjwt.entity.Role;
import com.alekseyz.testtask.springsecurityjwt.repository.RoleRepository;
import com.alekseyz.testtask.springsecurityjwt.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoleServiceImplementation implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> getStandartUserRole() {
        return List.of(roleRepository.findByName("USER").orElseThrow());
    }
}
