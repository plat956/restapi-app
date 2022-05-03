package com.epam.esm.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.OrderDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderDtoModelAssembler extends AbstractModelAssembler<OrderDto> {

    @Override
    public EntityModel<OrderDto> toModel(OrderDto entity) {
        Link selfLink = linkTo(methodOn(OrderController.class)
                .getOne(entity.getId()))
                .withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class)
                .getOneUser(entity.getUserId()))
                .withRel("userInfo");
        Link certificatesLink = linkTo(methodOn(OrderController.class)
                .getOrderCertificates(entity.getId(), null))
                .withRel("certificatesInfo").expand();
        entity.add(selfLink).add(userLink).add(certificatesLink);
        return EntityModel.of(entity);
    }

    @Override
    public EntityModel<OrderDto> toModelWithAllLink(OrderDto entity) {
        Link link = linkTo(methodOn(UserController.class)
                .getAllOrders(entity.getUserId(), null))
                .withRel("allUserOrders").expand();
        return toModelWithAdditionalLinks(entity, link);
    }
}
