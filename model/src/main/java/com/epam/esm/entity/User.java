package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class User extends BaseEntity<Long>{

    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String login;
    private String password;
    private Set<Order> orders = new HashSet<>();
}
