package com.epam.esm.service.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityMappingException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.mapper.EntityMapper;
import com.epam.esm.repository.OrderRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Spy
    private EntityMapper<OrderDto, Order> orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @ParameterizedTest
    @ValueSource(longs = 10L)
    void findByUserId(Long id) {
        int totalOrders = 10;
        when(orderRepository.findByUserId(anyLong(), any(Pageable.class))).thenAnswer(invocation -> {
            List<Order> orders = new ArrayList<>();
            for(int i = 0; i < totalOrders; i++) {
                Order o = new Order();
                o.setId((long)i);

                User u = new User();
                u.setId(invocation.getArgument(0));
                o.setUser(u);

                GiftCertificate c = new GiftCertificate();
                c.setId(1L);
                o.setGiftCertificates(List.of(c));

                o.setPurchaseTimestamp(1650528752084L);
                o.setCost(BigDecimal.valueOf(12.20));
                orders.add(o);
            }
            Pageable page = invocation.getArgument(1);
            return new PageImpl<>(orders, page, totalOrders);
        });

        Pageable page = PageRequest.of(2, 3);
        Page<OrderDto> result = orderService.findByUserId(id, page);

        verify(orderRepository).findByUserId(id, page);
        verify(orderMapper, times(totalOrders)).toDto(any());

        Integer actual = result.getTotalPages();
        assertEquals(4, actual);
    }

    @ParameterizedTest
    @ValueSource(longs = 12L)
    void create(Long userId) throws ServiceException, EntityMappingException {
        OrderDto o = new OrderDto();
        o.setCertificates(List.of(10L, 11L, 12L, 34L));
        o.setPurchaseTimestamp(1650528335256L);
        o.setCost(BigDecimal.valueOf(31.42));

        OrderDto spyDto = spy(o);
        orderService.create(userId, spyDto);

        verify(spyDto).setUserId(userId);
        verify(orderMapper).toEntity(spyDto);
        verify(orderRepository).save(any());
    }
}