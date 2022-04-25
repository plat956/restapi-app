package com.epam.esm.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.ArrayList;
import java.util.List;

public interface PagedModelAssembler<T> {

    String SELFT_NODE = "self";
    String FIRST_PAGE_NODE = "first";
    String PREV_PAGE_NODE = "prev";
    String NEXT_PAGE_NODE = "next";
    String LAST_PAGE_NODE = "last";

    void addLinks(EntityModel<T> resource);

    default EntityModel<T> toModel(T entity) {
        EntityModel<T> resource = EntityModel.of(entity);
        addLinks(resource);
        return resource;
    }

    default PagedModel<EntityModel<T>> toCollectionModel(PagedModel<T> entities) {
        List<EntityModel<T>> resourceList = new ArrayList<>();

        for (T entity : entities) {
            resourceList.add(toModel(entity));
        }

        return PagedModel.of(resourceList, entities.getMetadata());
    }
}
