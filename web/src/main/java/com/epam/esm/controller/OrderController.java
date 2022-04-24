package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateModelAssembler;
import com.epam.esm.assembler.OrderDtoModelAssembler;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.RequestedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public void setOrderDtoModelAssembler(OrderDtoModelAssembler orderDtoModelAssembler) {
        this.orderDtoModelAssembler = orderDtoModelAssembler;
    }

    @Autowired
    public void setCertificateModelAssembler(GiftCertificateModelAssembler certificateModelAssembler) {
        this.certificateModelAssembler = certificateModelAssembler;
    }

    /**
     * Get one order.
     *
     * @param id the order id
     * @return found order, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public EntityModel<OrderDto> getOne(@PathVariable("id") Long id) {
        OrderDto order = orderService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return orderDtoModelAssembler.toModelWithAllLink(order);
    }

    /**
     * Get order certificates.
     *
     * @param id the order id
     * @param page the requested page
     * @return found order certificates, otherwise empty list
     */
    @GetMapping("/{id}/certificates")
    public CollectionModel<EntityModel<GiftCertificate>> getOrderCertificates(
            @PathVariable("id") Long id, RequestedPage page) {
        List<GiftCertificate> certificates = certificateService.findByOrderIdPaginated(id, page);
        return certificateModelAssembler.toCollectionModel(certificates, id);
    }
}
