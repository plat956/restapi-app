package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.util.RequestedPage;

import java.util.List;

public interface OrderRepository extends BaseRepository<Long, Order>{

    List<Order> findByUserIdPaginated(Long id, RequestedPage page);
}
