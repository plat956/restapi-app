package com.epam.esm.mapper.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityMappingException;
import com.epam.esm.mapper.EntityMapper;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapperImpl implements EntityMapper<OrderDto, Order> {

    private UserRepository userRepository;
    private GiftCertificateRepository giftCertificateRepository;

    @Autowired
    public OrderMapperImpl(UserRepository userRepository, GiftCertificateRepository giftCertificateRepository) {
        this.userRepository = userRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    public Order toEntity(OrderDto dto) throws EntityMappingException {
        User user = userRepository.findOne(dto.getUserId())
                .orElseThrow(() -> new EntityMappingException(String.format("User with id = %d not found", dto.getUserId())));

        List<GiftCertificate> certificates = giftCertificateRepository.findByIdIn(dto.getCertificates());
        List<Long> certificateIds = certificates.stream().map(GiftCertificate::getId).toList();

        dto.getCertificates().removeAll(certificateIds);

        if(!dto.getCertificates().isEmpty()) {
            String idSequence = dto.getCertificates().stream().map(Object::toString)
                    .collect(Collectors.joining(", "));
            throw new EntityMappingException("Failed to find gift certificates: " + idSequence);
        }

        BigDecimal cost = certificates.stream().map(GiftCertificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setGiftCertificates(certificates);
        order.setCost(cost);
        order.setPurchaseTimestamp(Instant.now().toEpochMilli());
        return order;
    }

    @Override
    public OrderDto toDto(Order entity) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(entity.getId());
        orderDto.setUserId(entity.getUser().getId());
        List<Long> certificates = entity.getGiftCertificates().stream()
                .map(GiftCertificate::getId)
                .toList();
        orderDto.setCertificates(certificates);
        orderDto.setCost(entity.getCost());
        orderDto.setPurchaseTimestamp(entity.getPurchaseTimestamp());
        return orderDto;
    }
}
