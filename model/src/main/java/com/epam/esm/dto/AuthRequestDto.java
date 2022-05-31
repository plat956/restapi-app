package com.epam.esm.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AuthRequestDto {
    @Size(max = 255, message = "{validation.restrictions.auth.login}")
    @NotBlank(message = "{validation.error.auth.login}")
    private String login;

    @Size(max = 255, message = "{validation.restrictions.auth.password}")
    @NotBlank(message = "{validation.error.auth.password}")
    private String password;
}
