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
import com.epam.esm.util.RequestedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    private UserStatisticsDtoModelAssembler statModelAssembler;

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
    public void setStatModelAssembler(UserStatisticsDtoModelAssembler statModelAssembler) {
        this.statModelAssembler = statModelAssembler;
    }

    /**
     * Get one user.
     *
     * @param id the user id
     * @return found user, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public EntityModel<User> getOneUser(@PathVariable("id") Long id) {
        User user = userService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return userModelAssembler.toModelWithAllLink(user);
    }

    /**
     * Get all users.
     *
     * @param page the requested page
     * @param limit the requested records per page limit
     * @return all available users
     */
    @GetMapping
    public CollectionModel<EntityModel<User>> getAllUsers(@RequestParam(value = "page", required = false) Long page,
                                                          @RequestParam(value = "limit", required = false) Long limit) {
        PagedModel<User> users = userService.findAllPaginated(new RequestedPage(page, limit));
        return userModelAssembler.toCollectionModel(users);
    }

    /**
     * Get all user orders.
     *
     * @param userId the user id
     * @param page the requested page
     * @param limit the requested records per page limit
     * @return the all available user orders
     */
    @GetMapping("/{id}/orders")
    public CollectionModel<EntityModel<OrderDto>> getAllOrders(@PathVariable("id") Long userId,
                                                               @RequestParam(value = "page", required = false) Long page,
                                                               @RequestParam(value = "limit", required = false) Long limit) {
        PagedModel<OrderDto> orders = orderService.findByUserIdPaginated(userId, new RequestedPage(page, limit));
        return orderDtoModelAssembler.toCollectionModel(orders, userId);
    }

    /**
     * Create a new user order.
     *
     * @param userId   the user id
     * @param orderDto the order DTO with ids of obtaining certificates
     * @return the created order data
     */
    @PostMapping("/{id}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<OrderDto> createOrder(@PathVariable("id") Long userId, @RequestBody @Valid OrderDto orderDto) {
        try {
            OrderDto o = orderService.create(userId, orderDto);
            return orderDtoModelAssembler.toModelWithAllLink(o);
        } catch (ServiceException ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
    }

    /**
     * Get top users statistics.
     *
     * @param page the requested page
     * @param limit the requested records per page limit
     * @return the top users statistics
     */
    @GetMapping("/statistics")
    public PagedModel<EntityModel<UserStatisticsDto>> getStatistics(@RequestParam(value = "page", required = false) Long page,
                                                                    @RequestParam(value = "limit", required = false) Long limit) {
        PagedModel<UserStatisticsDto> statistics = userService.findUserStatistics(new RequestedPage(page, limit));
        return statModelAssembler.toCollectionModel(statistics);
    }
}
