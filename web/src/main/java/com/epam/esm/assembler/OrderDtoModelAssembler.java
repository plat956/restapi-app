package com.epam.esm.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.OrderDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderDtoModelAssembler implements SimpleRepresentationModelAssembler<OrderDto> {

    @Override
    public void addLinks(EntityModel<OrderDto> resource) {
        Link selfLink = linkTo(methodOn(OrderController.class)
                .getOne(resource.getContent().getId()))
                .withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class)
                .getOneUser(resource.getContent()
                .getUserId()))
                .withRel("userInfo");
        Link certificatesLink = linkTo(methodOn(OrderController.class)
                .getOrderCertificates(resource.getContent().getId(), null))
                .withRel("certificatesInfo");
        resource.add(selfLink).add(userLink).add(certificatesLink);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<OrderDto>> resources) {
    }

    public EntityModel<OrderDto> toModelWithAllLink(OrderDto entity) {
        EntityModel<OrderDto> model = SimpleRepresentationModelAssembler.super.toModel(entity);
        model.add(linkTo(methodOn(UserController.class)
                .getAllOrders(entity.getUserId(), null))
                .withRel("allUserOrders"));
        return model;
    }

    public CollectionModel<EntityModel<OrderDto>> toCollectionModel(Iterable<? extends OrderDto> entities, Long userId) {
        CollectionModel<EntityModel<OrderDto>> result = SimpleRepresentationModelAssembler.super.toCollectionModel(entities);
        Link selfLink = linkTo(methodOn(UserController.class)
                .getAllOrders(userId, null))
                .withSelfRel();
        result.add(selfLink);
        return result;
    }
}
