package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    /**
     * Get all gift certificates.
     *
     * @param tag    the tag name related to a certificate
     * @param search the part of name/description of a certificate
     * @param sort   the sequence of fields to sort the result,
     *               start with ordering type (+ ASC or - DESC) and a field to sort (available fields: createDate, lastUpdateDate, name).
     *               Eg. -createDate,+name
     * @return all suitable gift certificates
     */
    @GetMapping
    public List<GiftCertificate> getAll(@RequestParam(value = "tags", required = false) List<String> tags,
                                        @RequestParam(value = "search", required = false) String search,
                                        @RequestParam(value = "sort", required = false) List<String> sort) {
        return giftCertificateService.findAll(tags, search, sort);
    }

    /**
     * Get one gift certificate.
     *
     * @param id the id of gift certificate
     * @return found gift certificate, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public GiftCertificate getOne(@PathVariable("id") Long id) {
        return giftCertificateService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    /**
     * Save a gift certificate.
     *
     * @param giftCertificate the gift certificate json object
     * @return the gift certificate saved data
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificate save(@RequestBody @Valid GiftCertificate giftCertificate) {
        return giftCertificateService.save(giftCertificate);
    }

    /**
     * Update a gift certificate.
     *
     * @param id     the gift certificate id
     * @param source the json object with only fields to be updated
     * @return the updated gift certificate
     */
    @PatchMapping("/{id}")
    public GiftCertificate update(@PathVariable("id") Long id, @RequestBody GiftCertificate source) {
        try {
            return giftCertificateService.update(id, source);
        } catch (ServiceException ex) {
            throw new ResourceNotFoundException(id);
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
