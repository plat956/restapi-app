package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateModelAssembler;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.RequestedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * The Gift certificate REST API controller.
 */
@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private GiftCertificateService giftCertificateService;
    private GiftCertificateModelAssembler certificateModelAssembler;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @Autowired
    public void setCertificateModelAssembler(GiftCertificateModelAssembler certificateModelAssembler) {
        this.certificateModelAssembler = certificateModelAssembler;
    }

    /**
     * Get all gift certificates.
     *
     * @param tags    the list of tag names related to a certificate
     * @param search the part of name/description of a certificate
     * @param sort   the sequence of fields to sort the result,
     *               start with ordering type (+ ASC or - DESC) and a field to sort (available fields: createDate, lastUpdateDate, name).
     *               Eg. -createDate,+name
     * @param page the requested page
     * @param limit the requested records per page limit
     * @return all suitable gift certificates
     */
    @GetMapping
    public PagedModel<EntityModel<GiftCertificate>> getAll(@RequestParam(value = "tags", required = false) List<String> tags,
                                                                 @RequestParam(value = "search", required = false) String search,
                                                                 @RequestParam(value = "sort", required = false) List<String> sort,
                                                                 @RequestParam(value = "page", required = false) Long page,
                                                                 @RequestParam(value = "limit", required = false) Long limit) {
        PagedModel<GiftCertificate> certificates = giftCertificateService.findAllPaginated(tags, search, sort, new RequestedPage(page, limit));
        return certificateModelAssembler.toCollectionModel(certificates, tags, search, sort);
    }

    /**
     * Get one gift certificate.
     *
     * @param id the id of gift certificate
     * @return found gift certificate, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public EntityModel<GiftCertificate> getOne(@PathVariable("id") Long id) {
        GiftCertificate certificate = giftCertificateService.findOne(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return certificateModelAssembler.toModelWithAllLink(certificate);
    }

    /**
     * Save a gift certificate.
     *
     * @param giftCertificate the gift certificate json object
     * @return the gift certificate saved data
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<GiftCertificate> save(@RequestBody @Valid GiftCertificate giftCertificate) {
        GiftCertificate certificate = giftCertificateService.save(giftCertificate);
        return certificateModelAssembler.toModelWithAllLink(certificate);
    }

    /**
     * Update a gift certificate.
     *
     * @param id     the gift certificate id
     * @param source the json object with only fields to be updated
     * @return the updated gift certificate
     */
    @PatchMapping("/{id}")
    public EntityModel<GiftCertificate> update(@PathVariable("id") Long id,
                                               @RequestBody GiftCertificate source) {
        try {
            GiftCertificate certificate = giftCertificateService.update(id, source);
            return certificateModelAssembler.toModelWithAllLink(certificate);
        } catch (ServiceException ex) {
            throw new ResourceNotFoundException(id);
        }
    }

    /**
     * Unbind a tag from a gift certificate.
     *
     * @param certId     the gift certificate id
     * @param tagId     the tag id to be removed
     * @return the gift certificate without the removed tag
     */
    @DeleteMapping("/{certId}/tags/{tagId}")
    public EntityModel<GiftCertificate> unbindTag(@PathVariable("certId") Long certId,
                                                  @PathVariable("tagId") Long tagId) {
        try {
            GiftCertificate certificate = giftCertificateService.unbindTag(certId, tagId);
            return certificateModelAssembler.toModelWithAllLink(certificate);
        } catch (ServiceException ex) {
            throw new ResourceNotFoundException(certId);
        }
    }

    /**
     * Delete a gift certificate.
     *
     * @param id the gift certificate id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        giftCertificateService.delete(id);
    }
}
