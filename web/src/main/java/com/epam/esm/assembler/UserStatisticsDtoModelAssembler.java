package com.epam.esm.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserStatisticsDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserStatisticsDtoModelAssembler extends AbstractModelAssembler<UserStatisticsDto> {

    @Override
    public EntityModel<UserStatisticsDto> toModel(UserStatisticsDto entity) {
        Link userLink = linkTo(methodOn(UserController.class)
                .getOneUser(entity.getUserId()))
                .withRel("userInfo");
        Link tagLink = linkTo(methodOn(TagController.class)
                .getOne(entity.getTagId()))
                .withRel("tagInfo");
        entity.add(userLink).add(tagLink);
        return EntityModel.of(entity);
    }

    @Override
    public EntityModel<UserStatisticsDto> toModelWithAllLink(UserStatisticsDto entity) {
        return toModel(entity);
    }

    @Override
    public PagedModel<UserStatisticsDto> toPagedModel(Page<UserStatisticsDto> entities) {
        PagedModel<UserStatisticsDto> model = super.toPagedModel(entities);
        model.add(linkTo(methodOn(UserController.class).getAllUsers(null)).withRel("allUsers"));
        return model;
    }
}
