package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.util.RequestedPage;

import java.util.List;
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
    Optional<OrderDto> findOne(Long id);

    /**
     * Find orders by user id.
     *
     * @param id the user id
     * @param page the requested page
     * @return the list of orders or empty one
     */
    List<OrderDto> findByUserIdPaginated(Long id, RequestedPage page);

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
}
