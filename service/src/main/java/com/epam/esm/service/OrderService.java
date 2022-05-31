package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * The Order service interface.
 * Specifies a set of methods to get and manage orders
 */
public interface OrderService {
    /**
     * Find one order.
     *
     * @param id the order id
     * @return the optional with an order object if it exists, otherwise the empty optional
     */
    Optional<OrderDto> findById(Long id);

    /**
     * Find orders by user id.
     *
     * @param id the user id
     * @param pageable object containing page and size request parameters
     * @return the page object with orders or empty one
     */
    Page<OrderDto> findByUserId(Long id, Pageable pageable);

    /**
     * Create an order.
     *
     * @param userId the user-purchaser id
     * @param orderDto the order dto object with new order data
     * @return the saved order, represented as dto
     * @throws ServiceException the service exception if the user-purchaser
     * or any obtaining certificate from orderDto not found
     */
    OrderDto create(Long userId, OrderDto orderDto) throws ServiceException;

    /**
     * Check user access to an order.
     *
     * @param orderId the order id
     * @param userId  the user id
     * @return the boolean represents if access granted or not
     */
    boolean checkAccess(Long orderId, Long userId);
}
