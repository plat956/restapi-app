package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.Optional;

public interface TagService {
    Optional<Tag> findById(Long id);
}
