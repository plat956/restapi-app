package com.epam.esm.service.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<OrderDto> findUserOrders(Long id) {
        List<Order> orders = orderRepository.findByUserId(id);
        modelMapper.typeMap(Order.class, OrderDto.class).addMappings(mapper -> {
            mapper.skip(OrderDto::setCertificates);
        });
        return orders.stream().map(o -> {
            OrderDto orderDto = modelMapper.map(o, OrderDto.class);
            List<Long> certificates = o.getGiftCertificates().stream()
                    .map(GiftCertificate::getId)
                    .toList();
            orderDto.setCertificates(certificates);
            return orderDto;
        }).toList();
    }
}
