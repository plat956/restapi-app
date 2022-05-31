package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateModelAssembler;
import com.epam.esm.assembler.OrderDtoModelAssembler;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.security.userDetails.JwtUserDetails;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * The Order REST API controller.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;
    private GiftCertificateService certificateService;
    private MessageProvider messageProvider;
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

    @Autowired
    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    /**
     * Get one order.
     *
     * @param id the order id
     * @return found order, otherwise error response with 40401 status code
     */
    @PostAuthorize("returnObject.content.userId == authentication.principal.id OR hasRole('ADMIN')")
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
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal JwtUserDetails user) {
        if(!orderService.checkAccess(id, user.getId()) && !user.hasRole("ADMIN")) {
            throw new AccessDeniedException(messageProvider.getMessage("message.error.access-denied"));
        }
        Page<GiftCertificate> certificates = certificateService.findByOrderId(id, pageable);
        return certificateModelAssembler.toPagedModel(certificates, id);
    }

    /**
     * Create a new order for authenticated user.
     *
     * @param orderDto the order DTO with ids of obtaining certificates
     * @return the created order data
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<OrderDto> createOrder(@RequestBody @Valid OrderDto orderDto,
                                             @AuthenticationPrincipal JwtUserDetails user) {
        try {
            OrderDto o = orderService.create(user.getId(), orderDto);
            return orderDtoModelAssembler.toModelWithAllLink(o);
        } catch (ServiceException ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
    }
}
