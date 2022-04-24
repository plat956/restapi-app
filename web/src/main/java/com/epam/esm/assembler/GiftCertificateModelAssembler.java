package com.epam.esm.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateModelAssembler implements SimpleRepresentationModelAssembler<GiftCertificate> {

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

    @Override
    public void addLinks(CollectionModel<EntityModel<GiftCertificate>> resources) {
        Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                .getAll(null, null, null, null))
                .withSelfRel();
        resources.add(selfLink);
    }

    public EntityModel<GiftCertificate> toModelWithAllLink(GiftCertificate entity) {
        EntityModel<GiftCertificate> model = SimpleRepresentationModelAssembler.super.toModel(entity);
        model.add(linkTo(methodOn(GiftCertificateController.class)
                .getAll(null, null, null, null))
                .withRel("allCertificates"));
        return model;
    }

    public CollectionModel<EntityModel<GiftCertificate>> toCollectionModel(Iterable<? extends GiftCertificate> entities, Long orderId) {
        CollectionModel<EntityModel<GiftCertificate>> result = SimpleRepresentationModelAssembler.super.toCollectionModel(entities);
        result.removeLinks();
        Link selfLink = linkTo(methodOn(OrderController.class)
                .getOrderCertificates(orderId, null))
                .withSelfRel();
        Link orderLink = linkTo(methodOn(OrderController.class)
                .getOne(orderId))
                .withRel("order");
        result.add(selfLink).add(orderLink);
        return result;
    }
}
