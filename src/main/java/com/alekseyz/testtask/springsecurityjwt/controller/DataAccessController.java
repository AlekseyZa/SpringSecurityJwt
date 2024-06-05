package com.alekseyz.testtask.springsecurityjwt.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DataAccessController {

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public String unsecuredData() {
        return "Admin page (for user with role ADMIN)";
    }

    @PreAuthorize(value = "hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/user")
    public String securedData() {
        return "User page (for user with roles ADMIN or/and USER)";
    }

    @GetMapping("/public")
    public String adminData() {
        return "public page (for all users)";
    }

}
