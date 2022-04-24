package com.epam.esm.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.OrderDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderDtoModelAssembler implements PagedModelAssembler<OrderDto> {

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
                .getOrderCertificates(resource.getContent().getId(), null, null))
                .withRel("certificatesInfo").expand();
        resource.add(selfLink).add(userLink).add(certificatesLink);
    }

    public EntityModel<OrderDto> toModelWithAllLink(OrderDto entity) {
        EntityModel<OrderDto> model = PagedModelAssembler.super.toModel(entity);
        model.add(linkTo(methodOn(UserController.class)
                .getAllOrders(entity.getUserId(), null, null))
                .withRel("allUserOrders").expand());
        return model;
    }

    public PagedModel<EntityModel<OrderDto>> toCollectionModel(PagedModel<OrderDto> entities, Long userId) {
        PagedModel<EntityModel<OrderDto>> result = PagedModelAssembler.super.toCollectionModel(entities);
        result.removeLinks();

        PagedModel.PageMetadata metadata = entities.getMetadata();
        Long limit = metadata.getSize();
        Long page = metadata.getNumber();
        Long totalPages = metadata.getTotalPages();

        result.add(linkTo(methodOn(UserController.class)
                .getAllOrders(userId, page, limit))
                .withSelfRel().expand());

        if(page > 1) {
            result.add(linkTo(methodOn(UserController.class)
                    .getAllOrders(userId, 1L, limit))
                    .withRel(FIRST_PAGE_NODE).expand());
            result.add(linkTo(methodOn(UserController.class)
                    .getAllOrders(userId, page - 1L, limit))
                    .withRel(PREV_PAGE_NODE).expand());
        }

        if(page < totalPages) {
            result.add(linkTo(methodOn(UserController.class)
                    .getAllOrders(userId, page + 1L, limit))
                    .withRel(NEXT_PAGE_NODE).expand());
            result.add(linkTo(methodOn(UserController.class)
                    .getAllOrders(userId, totalPages, limit))
                    .withRel(LAST_PAGE_NODE).expand());
        }

        Link userLink = linkTo(methodOn(UserController.class)
                .getOneUser(userId))
                .withRel("user");
        result.add(userLink);
        return result;
    }
}
