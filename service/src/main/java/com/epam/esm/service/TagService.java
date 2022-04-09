package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface TagService {
    Optional<Tag> findOne(Long id);
    List<Tag> findAll();
    Tag save(Tag tag) throws ServiceException;
    void delete(Long id);
}
