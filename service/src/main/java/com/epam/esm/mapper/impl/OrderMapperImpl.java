package com.epam.esm.mapper.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityMappingException;
import com.epam.esm.mapper.EntityMapper;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.MessageProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapperImpl implements EntityMapper<OrderDto, Order> {

    private UserRepository userRepository;
    private GiftCertificateRepository giftCertificateRepository;
    private ModelMapper modelMapper;
    private MessageProvider messageProvider;

    @Autowired
    public OrderMapperImpl(UserRepository userRepository, GiftCertificateRepository giftCertificateRepository) {
        this.userRepository = userRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.typeMap(Order.class, OrderDto.class).addMappings(mapper -> {
            mapper.skip(OrderDto::setCertificates);
        });
    }

    @Autowired
    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @Override
    public Order toEntity(OrderDto dto) throws EntityMappingException {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityMappingException(String.format(messageProvider.getMessage("message.error.user-not-found"),
                        dto.getUserId())));

        List<GiftCertificate> certificates = new ArrayList<>(dto.getCertificates().size());

        for(Long certId: dto.getCertificates()) {
            GiftCertificate certificate = giftCertificateRepository.findById(certId)
                    .orElseThrow(() -> new EntityMappingException(messageProvider.getMessage("message.error.certificate-not-found") + certId));
            certificates.add(certificate);
        }

        BigDecimal cost = certificates.stream().map(GiftCertificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setGiftCertificates(certificates);
        order.setCost(cost);
        order.setPurchaseDate(LocalDateTime.now());
        return order;
    }

    @Override
    public OrderDto toDto(Order entity) {
        OrderDto orderDto = modelMapper.map(entity, OrderDto.class);
        List<Long> certificates = entity.getGiftCertificates().stream()
                .map(GiftCertificate::getId)
                .toList();
        orderDto.setCertificates(certificates);
        return orderDto;
    }
}
