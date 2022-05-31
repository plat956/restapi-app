package com.epam.esm.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

abstract class AbstractModelAssembler<T extends RepresentationModel> implements RepresentationModelAssembler<T, EntityModel<T>> {

    @Autowired
    private PagedResourcesAssembler pagedResourcesAssembler;

    public abstract EntityModel<T> toModelWithAllLink(T entity);

    protected EntityModel<T> toModelWithAdditionalLinks(T entity, Link... link) {
        EntityModel<T> model = toModel(entity);
        model.getContent().add(link);
        return model;
    }

    public PagedModel<T> toPagedModel(Page<T> entities) {
        return pagedResourcesAssembler.toModel(entities, this);
    }
}
