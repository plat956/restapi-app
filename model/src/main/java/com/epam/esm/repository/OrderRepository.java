package com.epam.esm.repository;

import com.epam.esm.entity.Order;

import java.util.List;

public interface OrderRepository extends BaseRepository<Long, Order>{

    List<Order> findByUserId(Long id);
}
