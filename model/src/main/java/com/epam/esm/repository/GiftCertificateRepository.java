package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.RequestedPage;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface GiftCertificateRepository extends BaseRepository<Long, GiftCertificate> {

    PagedModel<GiftCertificate> findAllPaginated(List<String> tags, String search,
                                                 List<String> sort, RequestedPage page);
    PagedModel<GiftCertificate> findByOrderIdPaginated(Long id, RequestedPage page);
}
