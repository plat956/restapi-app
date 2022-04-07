package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    Optional<GiftCertificate> findOne(Long id);
    List<GiftCertificate> findAll();
    GiftCertificate save(GiftCertificate tag);
    GiftCertificate update(GiftCertificate tag);
    void delete(Long id);
}
