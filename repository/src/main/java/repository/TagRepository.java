package repository;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagRepository extends BaseEntityRepository<Long, Tag> {
    List<Tag> findByGiftCertificateId(Long id);
}
