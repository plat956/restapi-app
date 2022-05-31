package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateModelAssembler;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
     * @param sorts   the sequence of fields to sort the result,
     *               start with ordering type (+ ASC or - DESC) and a field to sort (available fields: createDate, lastUpdateDate, name).
     *               Eg. -createDate,+name
     * @param pageable object containing page and size request parameters
     * @return all suitable gift certificates
     */
    @GetMapping
    public PagedModel<GiftCertificate> getAll(@RequestParam(value = "tags", required = false) List<String> tags,
                                              @RequestParam(value = "search", required = false) String search,
                                              @RequestParam(value = "sorts", required = false) List<String> sorts,
                                              @PageableDefault Pageable pageable) {
        Page<GiftCertificate> certificates = giftCertificateService.findAll(tags, search, sorts, pageable);
        return certificateModelAssembler.toPagedModel(certificates);
    }

    /**
     * Get one gift certificate.
     *
     * @param id the id of gift certificate
     * @return found gift certificate, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public EntityModel<GiftCertificate> getOne(@PathVariable("id") Long id) {
        GiftCertificate certificate = giftCertificateService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return certificateModelAssembler.toModelWithAllLink(certificate);
    }

    /**
     * Save a gift certificate.
     *
     * @param giftCertificate the gift certificate json object
     * @return the gift certificate saved data
     */
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        try {
            giftCertificateService.delete(id);
        } catch (ServiceException ex) {
            throw new ResourceNotFoundException(id);
        }
    }
}
