package com.epam.esm.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.entity.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements PagedModelAssembler<User> {

    @Override
    public void addLinks(EntityModel<User> resource) {
        Link selfLink = linkTo(methodOn(UserController.class)
                .getOneUser(resource.getContent().getId()))
                .withSelfRel();
        Link allUserOrdersLink = linkTo(methodOn(UserController.class)
                .getAllOrders(resource.getContent().getId(), null, null))
                .withRel("allUserOrders").expand();
        resource.add(selfLink).add(allUserOrdersLink);
    }

    public EntityModel<User> toModelWithAllLink(User entity) {
        EntityModel<User> model = PagedModelAssembler.super.toModel(entity);
        model.add(linkTo(methodOn(UserController.class)
                .getAllUsers(null, null))
                .withRel("allUsers").expand());
        return model;
    }

    public PagedModel<EntityModel<User>> toCollectionModel(PagedModel<User> entities) {
        PagedModel<EntityModel<User>> result = PagedModelAssembler.super.toCollectionModel(entities);
        result.removeLinks();

        PagedModel.PageMetadata metadata = entities.getMetadata();
        Long limit = metadata.getSize();
        Long page = metadata.getNumber();
        Long totalPages = metadata.getTotalPages();

        result.add(linkTo(methodOn(UserController.class)
                .getAllUsers(page, limit))
                .withSelfRel().expand());

        if(page > 1L) {
            result.add(linkTo(methodOn(UserController.class)
                    .getAllUsers(1L, limit))
                    .withRel(FIRST_PAGE_NODE).expand());
            result.add(linkTo(methodOn(UserController.class)
                    .getAllUsers(page - 1L, limit))
                    .withRel(PREV_PAGE_NODE).expand());
        }

        if(page < totalPages) {
            result.add(linkTo(methodOn(UserController.class)
                    .getAllUsers(page + 1L, limit))
                    .withRel(NEXT_PAGE_NODE).expand());
            result.add(linkTo(methodOn(UserController.class)
                    .getAllUsers(totalPages, limit))
                    .withRel(LAST_PAGE_NODE).expand());
        }
        return result;
    }
}
