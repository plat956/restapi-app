package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * The User REST API controller.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private OrderService orderService;

    @Autowired
    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    /**
     * Get one user.
     *
     * @param id the user id
     * @return found user, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public User getOneUser(@PathVariable("id") Long id) {
        return userService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    /**
     * Get all users.
     *
     * @return all available users
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    /**
     * Get all user orders.
     *
     * @param userId the user id
     * @return the all available user orders
     */
    @GetMapping("/{id}/orders")
    public List<OrderDto> getAllOrders(@PathVariable("id") Long userId) {
        return orderService.findByUserId(userId);
    }

    /**
     * Create a new user order.
     *
     * @param userId   the user id
     * @param orderDto the order DTO with ids of obtaining certificates
     * @return the created order data
     */
    @PostMapping("/{id}/orders")
    public OrderDto createOrder(@PathVariable("id") Long userId, @RequestBody @Valid OrderDto orderDto) {
        try {
            return orderService.create(userId, orderDto);
        } catch (ServiceException ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
    }
}