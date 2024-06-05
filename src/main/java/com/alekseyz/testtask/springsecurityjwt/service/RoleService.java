package com.alekseyz.testtask.springsecurityjwt.service;

import com.alekseyz.testtask.springsecurityjwt.entity.Role;

import java.util.List;

public interface RoleService {

    List<Role> getStandartUserRole();
}
