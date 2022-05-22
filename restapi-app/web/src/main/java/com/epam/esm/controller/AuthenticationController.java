package com.epam.esm.controller;

import com.epam.esm.dto.AuthRequestDto;
import com.epam.esm.dto.AuthResponseDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceDuplicateException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider,
                                    UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping
    public AuthResponseDto signIn(@RequestBody @Valid AuthRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getLogin(), requestDto.getPassword())
        );
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(user.getUsername());
        return new AuthResponseDto(user.getUsername(), token);
    }

    @PostMapping("/signup")
    @ResponseStatus(value = HttpStatus.CREATED)
    public User signUp(@RequestBody @Valid UserDto userDto) {
        try {
            return userService.signUp(userDto);
        } catch (ServiceException e) {
            throw new ResourceDuplicateException("login", userDto.getLogin());
        }
    }
}
