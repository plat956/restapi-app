package repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends BaseEntityRepository<Long, Tag> {
    List<Tag> findByGiftCertificateId(Long id);
    Optional<Tag> findByName(String name);
}
