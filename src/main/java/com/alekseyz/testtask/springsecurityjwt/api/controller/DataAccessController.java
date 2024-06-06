package com.alekseyz.testtask.springsecurityjwt.api.controller;


import com.alekseyz.testtask.springsecurityjwt.api.DataAccessApi;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DataAccessController implements DataAccessApi {

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @Override
    public String adminData() {
        return "Admin page (for user with role ADMIN)";
    }

    @PreAuthorize(value = "hasAnyAuthority('USER', 'ADMIN')")
    @Override
    public String userData() {
        return "User page (for user with roles ADMIN or/and USER)";
    }

    @Override
    public String publicData() {

        return "public page (for all users including anonymous users)";
    }

}
