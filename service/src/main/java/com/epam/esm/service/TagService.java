package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * The Tag service interface.
 * Specifies a set of methods to get and manage tags
 */
public interface TagService {
    /**
     * Find one tag.
     *
     * @param id the tag id
     * @return the optional with a tag object if it exists, otherwise the empty optional
     */
    Optional<Tag> findById(Long id);

    /**
     * Find all tags.
     * @param pageable object containing page and size request parameters
     * @return the page object with tags or empty one
     */
    Page<Tag> findAll(Pageable pageable);

    /**
     * Save a tag.
     *
     * @param tag the tag object
     * @return the saved tag
     * @throws ServiceException the service exception if this tag already exists in database
     */
    Tag save(Tag tag) throws ServiceException;

    /**
     * Delete a tag.
     *
     * @param id the tag id
     */
    void delete(Long id) throws ServiceException;
}
