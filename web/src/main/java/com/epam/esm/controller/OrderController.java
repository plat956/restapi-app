package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Order REST API controller.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Get one order.
     *
     * @param id the order id
     * @return found order, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public OrderDto getOne(@PathVariable("id") Long id) {
        return orderService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }
}
