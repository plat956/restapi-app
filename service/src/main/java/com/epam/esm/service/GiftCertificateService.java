package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    Optional<GiftCertificate> findOne(Long id);
    List<GiftCertificate> findAll();
    List<GiftCertificate> findAll(String certificate, String search, String sort);
    GiftCertificate save(GiftCertificate certificate);
    GiftCertificate update(Long id, GiftCertificate certificate) throws ServiceException;
    void delete(Long id);
}
