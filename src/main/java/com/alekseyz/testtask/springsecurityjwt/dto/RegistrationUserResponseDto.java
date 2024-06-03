package com.alekseyz.testtask.springsecurityjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class RegistrationUserResponseDto {
    private Long id;
    private String username;
}
