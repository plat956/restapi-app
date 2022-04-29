package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.util.RequestedPage;
import org.springframework.hateoas.PagedModel;

public interface OrderRepository extends BaseRepository<Long, Order>{

    PagedModel<Order> findByUserIdPaginated(Long id, RequestedPage page);
}
