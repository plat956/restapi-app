package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateModelAssembler;
import com.epam.esm.assembler.OrderDtoModelAssembler;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Order REST API controller.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;
    private GiftCertificateService certificateService;
    private OrderDtoModelAssembler orderDtoModelAssembler;
    private GiftCertificateModelAssembler certificateModelAssembler;

    @Autowired
    public OrderController(OrderService orderService, GiftCertificateService certificateService) {
        this.orderService = orderService;
        this.certificateService = certificateService;
    }

    @Autowired
    public void setCertificateModelAssembler(GiftCertificateModelAssembler certificateModelAssembler) {
        this.certificateModelAssembler = certificateModelAssembler;
    }

    @Autowired
    public void setOrderDtoModelAssembler(OrderDtoModelAssembler orderDtoModelAssembler) {
        this.orderDtoModelAssembler = orderDtoModelAssembler;
    }

    /**
     * Get one order.
     *
     * @param id the order id
     * @return found order, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public EntityModel<OrderDto> getOne(@PathVariable("id") Long id) {
        OrderDto order = orderService.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return orderDtoModelAssembler.toModelWithAllLink(order);
    }

    /**
     * Get order certificates.
     *
     * @param id the order id
     * @param pageable object containing page and size request parameters
     * @return found order certificates, otherwise empty list
     */
    @GetMapping("/{id}/certificates")
    public PagedModel<GiftCertificate> getOrderCertificates(
            @PathVariable("id") Long id,
            @PageableDefault Pageable pageable) {
        Page<GiftCertificate> certificates = certificateService.findByOrderId(id, pageable);
        return certificateModelAssembler.toPagedModel(certificates, id);
    }
}
