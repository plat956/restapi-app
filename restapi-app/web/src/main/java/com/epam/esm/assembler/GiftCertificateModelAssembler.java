package com.epam.esm.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateModelAssembler extends AbstractModelAssembler<GiftCertificate> {

    @Override
    public EntityModel<GiftCertificate> toModel(GiftCertificate entity) {
        Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                .getOne(entity.getId()))
                .withSelfRel();
        entity.add(selfLink);

        entity.getTags()
            .forEach(t -> {
                if(!t.hasLink("self")) {
                    t.add(linkTo(methodOn(TagController.class)
                            .getOne(t.getId()))
                            .withSelfRel());
                }
            });

        return EntityModel.of(entity);
    }

    @Override
    public EntityModel<GiftCertificate> toModelWithAllLink(GiftCertificate entity) {
        Link link = linkTo(methodOn(GiftCertificateController.class)
                .getAll(null, null, null, null))
                .withRel("allCertificates").expand();
        return toModelWithAdditionalLinks(entity, link);
    }

    public PagedModel<GiftCertificate> toPagedModel(Page<GiftCertificate> entities, Long orderId) {
        PagedModel<GiftCertificate> model = toPagedModel(entities);
        model.add(linkTo(methodOn(OrderController.class).getOne(orderId)).withRel("order"));
        return model;
    }
}
