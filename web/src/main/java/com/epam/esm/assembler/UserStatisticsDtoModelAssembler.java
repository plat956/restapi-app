package com.epam.esm.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserStatisticsDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserStatisticsDtoModelAssembler implements PagedModelAssembler<UserStatisticsDto>{

    @Override
    public void addLinks(EntityModel<UserStatisticsDto> resource) {
        Link userLink = linkTo(methodOn(UserController.class)
                .getOneUser(resource.getContent()
                        .getUserId()))
                .withRel("userInfo");
        resource.add(userLink);

        resource.getContent().getTags().forEach(t -> {
            if(!t.hasLink("tagInfo")) {
                Link tagLink = linkTo(methodOn(TagController.class)
                        .getOne(t.getTagId()))
                        .withRel("tagInfo");
                t.add(tagLink);
            }
        });
    }

    @Override
    public PagedModel<EntityModel<UserStatisticsDto>> toCollectionModel(PagedModel<UserStatisticsDto> entities) {
        PagedModel<EntityModel<UserStatisticsDto>> result = PagedModelAssembler.super.toCollectionModel(entities);
        result.removeLinks();

        PagedModel.PageMetadata metadata = entities.getMetadata();
        Long limit = metadata.getSize();
        Long page = metadata.getNumber();
        Long totalPages = metadata.getTotalPages();

        result.add(linkTo(methodOn(UserController.class)
                .getStatistics(page, limit))
                .withSelfRel().expand());

        if(page > 1) {
            result.add(linkTo(methodOn(UserController.class)
                    .getStatistics(1L, limit))
                    .withRel(FIRST_PAGE_NODE).expand());
            result.add(linkTo(methodOn(UserController.class)
                    .getStatistics(page - 1L, limit))
                    .withRel(PREV_PAGE_NODE).expand());
        }

        if(page < totalPages) {
            result.add(linkTo(methodOn(UserController.class)
                    .getStatistics(page + 1L, limit))
                    .withRel(NEXT_PAGE_NODE).expand());
            result.add(linkTo(methodOn(UserController.class)
                    .getStatistics(totalPages, limit))
                    .withRel(LAST_PAGE_NODE).expand());
        }

        Link usersLink = linkTo(methodOn(UserController.class)
                .getAllUsers(null, null))
                .withRel("allUsers").expand();
        result.add(usersLink);
        return result;
    }
}
