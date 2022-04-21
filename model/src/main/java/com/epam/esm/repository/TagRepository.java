package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends BaseRepository<Long, Tag> {
    List<Tag> findByGiftCertificateId(Long id);
    Optional<Tag> findByName(String name);
}
