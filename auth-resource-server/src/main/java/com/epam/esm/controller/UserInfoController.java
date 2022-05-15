package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.User;
import com.epam.esm.security.CustomUserDetails;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserInfoController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public User getProfile(@AuthenticationPrincipal CustomUserDetails user) {
        return userService.findById(user.getId()).get();
    }

    @GetMapping("/orders")
    public List<OrderDto> getAllOrders(@AuthenticationPrincipal CustomUserDetails user, HttpServletRequest request) {
        return userService.findUserOrders(user.getId());
    }
}
