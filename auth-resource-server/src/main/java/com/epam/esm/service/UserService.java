package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);
    List<OrderDto> findUserOrders(Long id);
}
