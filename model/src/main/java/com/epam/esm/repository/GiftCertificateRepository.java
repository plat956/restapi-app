package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import static com.epam.esm.repository.QueryStorage.CERTIFICATES_COUNT_BY_ORDER_ID;
import static com.epam.esm.repository.QueryStorage.CERTIFICATES_FIND_BY_ORDER_ID;

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long>, JpaSpecificationExecutor<GiftCertificate> {

    @Query(value = CERTIFICATES_FIND_BY_ORDER_ID,
            countQuery = CERTIFICATES_COUNT_BY_ORDER_ID,
            nativeQuery = true
    )
    Page<GiftCertificate> findByOrderId(@Param("id") Long id, Pageable page);
}
