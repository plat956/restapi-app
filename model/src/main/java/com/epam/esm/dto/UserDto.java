package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UserDto {
    @NotBlank(message = "{validation.error.user.fname}")
    @Size(max = 255, message = "{validation.restrictions.user.fname}")
    private String firstName;

    @NotBlank(message = "{validation.error.user.lname}")
    @Size(max = 255, message = "{validation.restrictions.user.lname}")
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotBlank(message = "{validation.error.auth.login}")
    @Size(max = 255, message = "{validation.restrictions.auth.login}")
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "{validation.error.auth.password}")
    @Size(max = 255, message = "{validation.restrictions.auth.password}")
    private String password;
}
