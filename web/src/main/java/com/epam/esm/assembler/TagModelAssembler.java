package com.epam.esm.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.entity.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler implements PagedModelAssembler<Tag> {

    @Override
    public void addLinks(EntityModel<Tag> resource) {
        Link selfLink = linkTo(methodOn(TagController.class)
                .getOne(resource.getContent().getId()))
                .withSelfRel();
        resource.add(selfLink);
    }

    public EntityModel<Tag> toModelWithAllLink(Tag entity) {
        EntityModel<Tag> model = PagedModelAssembler.super.toModel(entity);
        model.add(linkTo(methodOn(TagController.class)
                .getAll(null, null))
                .withRel("allTags").expand());
        return model;
    }

    public PagedModel<EntityModel<Tag>> toCollectionModel(PagedModel<Tag> entities) {
        PagedModel<EntityModel<Tag>> result = PagedModelAssembler.super.toCollectionModel(entities);
        result.removeLinks();

        PagedModel.PageMetadata metadata = entities.getMetadata();
        Long limit = metadata.getSize();
        Long page = metadata.getNumber();
        Long totalPages = metadata.getTotalPages();

        result.add(linkTo(methodOn(TagController.class)
                .getAll(page, limit))
                .withSelfRel().expand());

        if(page > 1L) {
            result.add(linkTo(methodOn(TagController.class)
                    .getAll(1L, limit))
                    .withRel(FIRST_PAGE_NODE).expand());
            result.add(linkTo(methodOn(TagController.class)
                    .getAll(page - 1L, limit))
                    .withRel(PREV_PAGE_NODE).expand());
        }

        if(page < totalPages) {
            result.add(linkTo(methodOn(TagController.class)
                    .getAll(page + 1L, limit))
                    .withRel(NEXT_PAGE_NODE).expand());
            result.add(linkTo(methodOn(TagController.class)
                    .getAll(totalPages, limit))
                    .withRel(LAST_PAGE_NODE).expand());
        }
        return result;
    }
}
