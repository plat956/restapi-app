package com.epam.esm.controller;

import com.epam.esm.assembler.OrderDtoModelAssembler;
import com.epam.esm.assembler.UserModelAssembler;
import com.epam.esm.assembler.UserStatisticsDtoModelAssembler;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserStatisticsDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * The User REST API controller.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private OrderService orderService;
    private UserModelAssembler userModelAssembler;
    private OrderDtoModelAssembler orderDtoModelAssembler;
    private UserStatisticsDtoModelAssembler userStatisticsDtoModelAssembler;

    @Autowired
    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @Autowired
    public void setUserModelAssembler(UserModelAssembler userModelAssembler) {
        this.userModelAssembler = userModelAssembler;
    }

    @Autowired
    public void setOrderDtoModelAssembler(OrderDtoModelAssembler orderDtoModelAssembler) {
        this.orderDtoModelAssembler = orderDtoModelAssembler;
    }

    @Autowired
    public void setUserStatisticsDtoModelAssembler(UserStatisticsDtoModelAssembler userStatisticsDtoModelAssembler) {
        this.userStatisticsDtoModelAssembler = userStatisticsDtoModelAssembler;
    }

    /**
     * Get one user.
     *
     * @param id the user id
     * @return found user, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public EntityModel<User> getOneUser(@PathVariable("id") Long id) {
        User user = userService.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return userModelAssembler.toModelWithAllLink(user);
    }

    /**
     * Get all users.
     *
     * @param pageable object containing page and size request parameters
     * @return all available users
     */
    @GetMapping
    public PagedModel<User> getAllUsers(@PageableDefault Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        return userModelAssembler.toPagedModel(users);
    }

    /**
     * Get all user orders.
     *
     * @param userId the user id
     * @param pageable object containing page and size request parameters
     * @return the all available user orders
     */
    @PreAuthorize("#userId == authentication.principal.id OR hasRole('ADMIN')")
    @GetMapping("/{id}/orders")
    public PagedModel<OrderDto> getAllOrders(@PathVariable("id") Long userId,
                                             @PageableDefault Pageable pageable) {
        Page<OrderDto> orders = orderService.findByUserId(userId, pageable);
        return orderDtoModelAssembler.toPagedModel(orders);
    }

    /**
     * Change a user status.
     *
     * @param id the requested user id
     * @param active a new status of a user account
     * @return the updated user model
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public EntityModel<User> changeStatus(@PathVariable("id") Long id,
                                          @RequestParam("active") Boolean active) {
        try {
            User user = userService.changeStatus(id, active ? User.Status.ACTIVE : User.Status.LOCKED);
            return userModelAssembler.toModelWithAllLink(user);
        } catch (ServiceException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    /**
     * Get top users statistics.
     *
     * @param pageable object containing page and size request parameters
     * @return the top users statistics
     */
    @GetMapping("/statistics")
    public PagedModel<UserStatisticsDto> getStatistics(@PageableDefault Pageable pageable) {
        Page<UserStatisticsDto> userStatisticsDtos = userService.findUserStatistics(pageable);
        return userStatisticsDtoModelAssembler.toPagedModel(userStatisticsDtos);
    }
}
