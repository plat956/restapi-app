package com.epam.esm.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AuthRequestDto {
    @Size(max = 255, message = "Login must be less than or equal to 255 chars")
    @NotBlank(message = "Please provide a login")
    private String login;

    @Size(max = 255, message = "Password must be less than or equal to 255 chars")
    @NotBlank(message = "Please provide a password")
    private String password;
}
