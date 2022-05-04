package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomGiftCertificateRepository {

    Page<GiftCertificate> findAll(List<String> tags, String search, List<String> sorts, Pageable pageable);
}
