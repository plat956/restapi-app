package com.epam.esm.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.entity.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler extends AbstractModelAssembler<Tag> {

    @Override
    public EntityModel<Tag> toModel(Tag entity) {
        Link selfLink = linkTo(methodOn(TagController.class)
                .getOne(entity.getId()))
                .withSelfRel();
        entity.add(selfLink);
        return EntityModel.of(entity);
    }

    @Override
    public EntityModel<Tag> toModelWithAllLink(Tag entity) {
        Link link = linkTo(methodOn(TagController.class)
                .getAll(null))
                .withRel("allTags").expand();
        return toModelWithAdditionalLinks(entity, link);
    }
}
