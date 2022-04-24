package com.epam.esm.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.entity.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler implements SimpleRepresentationModelAssembler<Tag> {

    @Override
    public void addLinks(EntityModel<Tag> resource) {
        Link selfLink = linkTo(methodOn(TagController.class)
                .getOne(resource.getContent().getId()))
                .withSelfRel();
        resource.add(selfLink);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Tag>> resources) {
        Link selfLink = linkTo(methodOn(TagController.class)
                .getAll(null))
                .withSelfRel();
        resources.add(selfLink);
    }

    public EntityModel<Tag> toModelWithAllLink(Tag entity) {
        EntityModel<Tag> model = SimpleRepresentationModelAssembler.super.toModel(entity);
        model.add(linkTo(methodOn(TagController.class)
                .getAll(null))
                .withRel("allTags"));
        return model;
    }
}
