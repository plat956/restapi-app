package com.epam.esm.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateModelAssembler implements PagedModelAssembler<GiftCertificate> {

    @Override
    public void addLinks(EntityModel<GiftCertificate> resource) {
        Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                .getOne(resource.getContent().getId()))
                .withSelfRel();
        resource.add(selfLink);
        resource.getContent().getTags()
                .forEach(t ->
                        t.add(linkTo(methodOn(TagController.class)
                                .getOne(t.getId()))
                                .withSelfRel()));
    }

    public EntityModel<GiftCertificate> toModelWithAllLink(GiftCertificate entity) {
        EntityModel<GiftCertificate> model = PagedModelAssembler.super.toModel(entity);
        model.add(linkTo(methodOn(GiftCertificateController.class)
                .getAll(null, null, null, null, null))
                .withRel("allCertificates").expand());
        return model;
    }

    public PagedModel<EntityModel<GiftCertificate>> toCollectionModel(PagedModel<GiftCertificate> entities, List<String> tags,
                                                                            String search, List<String> sort) {
        PagedModel<EntityModel<GiftCertificate>> result = PagedModelAssembler.super.toCollectionModel(entities);
        result.removeLinks();

        PagedModel.PageMetadata metadata = entities.getMetadata();
        Long limit = metadata.getSize();
        Long page = metadata.getNumber();
        Long totalPages = metadata.getTotalPages();

        result.add(linkTo(methodOn(GiftCertificateController.class)
                .getAll(tags, search, sort, page, limit))
                .withSelfRel().expand());

        if(page > 1L) {
            result.add(linkTo(methodOn(GiftCertificateController.class)
                    .getAll(tags, search, sort, 1L, limit))
                    .withRel(FIRST_PAGE_NODE).expand());
            result.add(linkTo(methodOn(GiftCertificateController.class)
                    .getAll(tags, search, sort, page - 1L, limit))
                    .withRel(PREV_PAGE_NODE).expand());
        }

        if(page < totalPages) {
            result.add(linkTo(methodOn(GiftCertificateController.class)
                    .getAll(tags, search, sort, page + 1L, limit))
                    .withRel(NEXT_PAGE_NODE).expand());
            result.add(linkTo(methodOn(GiftCertificateController.class)
                    .getAll(tags, search, sort, totalPages, limit))
                    .withRel(LAST_PAGE_NODE).expand());
        }
        return result;
    }

    public PagedModel<EntityModel<GiftCertificate>> toCollectionModel(PagedModel<GiftCertificate> entities, Long orderId) {
        PagedModel<EntityModel<GiftCertificate>> result = PagedModelAssembler.super.toCollectionModel(entities);
        result.removeLinks();

        PagedModel.PageMetadata metadata = entities.getMetadata();
        Long limit = metadata.getSize();
        Long page = metadata.getNumber();
        Long totalPages = metadata.getTotalPages();

        result.add(linkTo(methodOn(OrderController.class)
                .getOrderCertificates(orderId, page, limit))
                .withSelfRel().expand());

        if(page > 1L) {
            result.add(linkTo(methodOn(OrderController.class)
                    .getOrderCertificates(orderId, 1L, limit))
                    .withRel(FIRST_PAGE_NODE).expand());
            result.add(linkTo(methodOn(OrderController.class)
                    .getOrderCertificates(orderId, page - 1L, limit))
                    .withRel(PREV_PAGE_NODE).expand());
        }

        if(page < totalPages) {
            result.add(linkTo(methodOn(OrderController.class)
                    .getOrderCertificates(orderId, page + 1L, limit))
                    .withRel(NEXT_PAGE_NODE).expand());
            result.add(linkTo(methodOn(OrderController.class)
                    .getOrderCertificates(orderId, totalPages, limit))
                    .withRel(LAST_PAGE_NODE).expand());
        }

        Link orderLink = linkTo(methodOn(OrderController.class).getOne(orderId)).withRel("order");
        result.add(orderLink);
        return result;
    }
}
