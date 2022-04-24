package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.util.RequestedPage;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.Optional;

/**
 * The Gift certificate service interface.
 * Specifies a set of methods to get and manage gift certificates
 */
public interface GiftCertificateService {
    /**
     * Find one gift certificate.
     *
     * @param id the gift certificate id
     * @return the optional with a gift certificate object if it exists, otherwise the empty optional
     */
    Optional<GiftCertificate> findOne(Long id);

    /**
     * Find all gift certificates.
     *
     * @param page the requested page
     * @return the paged model with a list of gift certificates or empty one
     */
    PagedModel<GiftCertificate> findAllPaginated(RequestedPage page);

    /**
     * Find all gift certificates parametrized.
     *
     * @param tags the tag name related to a certificate
     * @param search      the part of name/description of a certificate
     * @param sort        the sequence of fields to sort the result,
     *                    start with ordering type (+ ASC or - DESC) and a field to sort (available fields: createDate, lastUpdateDate, name).
     *                    Eg. -createDate,+name
     * @param page the requested page
     * @return the paged model with a list of suitable gift certificates
     */
    PagedModel<GiftCertificate> findAllPaginated(List<String> tags, String search, List<String> sort, RequestedPage page);

    /**
     * Save a gift certificate.
     *
     * @param certificate the certificate object
     * @return the saved gift certificate
     */
    GiftCertificate save(GiftCertificate certificate);

    /**
     * Update a gift certificate.
     *
     * @param id          the gift certificate id
     * @param certificate the certificate object
     * @return the updated gift certificate
     * @throws ServiceException the service exception, if no any gift certificates found with this id
     */
    GiftCertificate update(Long id, GiftCertificate certificate) throws ServiceException;

    /**
     * Delete a gift certificate.
     *
     * @param id the gift certificate id
     */
    void delete(Long id);

    /**
     * Unbind a tag from a certificate.
     *
     * @param certificateId the gift certificate id
     * @param tagId the tag id to be removed
     * @return the gift certificate without the removed tag
     * @throws ServiceException the service exception, if no any gift certificates found with this id
     */
    GiftCertificate unbindTag(Long certificateId, Long tagId) throws ServiceException;


    /**
     * Find gift certificates by order id.
     *
     * @param id   the gift certificate id
     * @param page the requested page
     * @return the paged model with a list of found certificates
     */
    PagedModel<GiftCertificate> findByOrderIdPaginated(Long id, RequestedPage page);
}
