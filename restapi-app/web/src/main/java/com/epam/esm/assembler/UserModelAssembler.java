package com.epam.esm.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.entity.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler extends AbstractModelAssembler<User> {

    @Override
    public EntityModel<User> toModel(User entity) {
        Link selfLink = linkTo(methodOn(UserController.class)
                .getOneUser(entity.getId()))
                .withSelfRel();
        Link allUserOrdersLink = linkTo(methodOn(UserController.class)
                .getAllOrders(entity.getId(), null))
                .withRel("allUserOrders").expand();
        entity.add(selfLink).add(allUserOrdersLink);
        return EntityModel.of(entity);
    }

    @Override
    public EntityModel<User> toModelWithAllLink(User entity) {
        EntityModel<User> model = toModel(entity);
        model.getContent().add(linkTo(methodOn(UserController.class)
                .getAllUsers(null))
                .withRel("allUsers").expand());
        return model;
    }
}
