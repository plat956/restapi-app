package com.epam.esm.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.entity.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements SimpleRepresentationModelAssembler<User> {

    @Override
    public void addLinks(EntityModel<User> resource) {
        Link selfLink = linkTo(methodOn(UserController.class)
                .getOneUser(resource.getContent().getId()))
                .withSelfRel();
        Link allUserOrdersLink = linkTo(methodOn(UserController.class)
                .getAllOrders(resource.getContent().getId(), null))
                .withRel("allUserOrders");
        resource.add(selfLink).add(allUserOrdersLink);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<User>> resources) {
        Link selfLink = linkTo(methodOn(UserController.class)
                .getAllUsers(null))
                .withSelfRel();
        resources.add(selfLink);
    }

    public EntityModel<User> toModelWithAllLink(User entity) {
        EntityModel<User> model = SimpleRepresentationModelAssembler.super.toModel(entity);
        model.add(linkTo(methodOn(UserController.class)
                .getAllUsers(null))
                .withRel("allUsers"));
        return model;
    }
}
