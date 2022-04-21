package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateRepository extends BaseRepository<Long, GiftCertificate> {
    List<GiftCertificate> findAll(String tag, String search, String sort);
}
