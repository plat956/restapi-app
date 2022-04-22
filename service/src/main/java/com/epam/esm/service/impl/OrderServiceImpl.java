package com.epam.esm.service.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.EntityMappingException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.mapper.EntityMapper;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private EntityMapper<OrderDto, Order> orderMapper;
    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(EntityMapper<OrderDto, Order> orderMapper, OrderRepository orderRepository) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<OrderDto> findOne(Long id) {
        Optional<Order> order = orderRepository.findOne(id);
        if(order.isPresent()) {
            return Optional.of(orderMapper.toDto(order.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderDto> findByUserId(Long id) {
        List<Order> orders = orderRepository.findByUserId(id);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    @Override
    public OrderDto create(Long userId, OrderDto orderDto) throws ServiceException {
        orderDto.setUserId(userId);
        Order order;
        try {
            order = orderMapper.toEntity(orderDto);
        } catch (EntityMappingException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }
}
