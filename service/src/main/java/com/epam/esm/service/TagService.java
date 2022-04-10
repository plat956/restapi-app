package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;

import java.util.List;
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
    Optional<Tag> findOne(Long id);

    /**
     * Find all tags.
     *
     * @return the list of tags or empty one
     */
    List<Tag> findAll();

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
    void delete(Long id);
}
