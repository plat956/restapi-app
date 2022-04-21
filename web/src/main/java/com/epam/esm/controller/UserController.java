package com.epam.esm.controller;

import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable("id") Long id) {
        return userService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }
}
