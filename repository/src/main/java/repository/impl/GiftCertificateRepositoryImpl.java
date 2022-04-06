package repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import repository.GiftCertificateRepository;
import repository.TagRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String FIND_BY_ID_QUERY = """
        SELECT id, name, description, price, duration, create_date, last_update_date 
        FROM gift_certificate WHERE id = ?""";
    private static final String FIND_ALL_QUERY = """
        SELECT id, name, description, price, duration, create_date, last_update_date 
        FROM gift_certificate""";
    private static final String CREATE_CERTIFICATE_TAG_RELATIONSHIP_QUERY = """
        INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)""";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private TagRepository tagRepository;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setSimpleJdbcInsert(SimpleJdbcInsert simpleJdbcInsert) {
        this.simpleJdbcInsert = simpleJdbcInsert;
        this.simpleJdbcInsert.withTableName("gift_certificate").usingGeneratedKeyColumns("id");
    }

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Optional<GiftCertificate> findOne(Long id) {
        try {
            GiftCertificate certificate = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,
                    new GiftCertificateRowMapper(), id);
            return Optional.of(certificate);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, new GiftCertificateRowMapper());
    }

    @Override
    @Transactional
    public GiftCertificate save(GiftCertificate certificate) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", certificate.getName());
        params.put("description", certificate.getDescription());
        params.put("price", certificate.getPrice());
        params.put("duration", certificate.getDuration());
        params.put("create_date", certificate.getCreateDate());
        params.put("last_update_date", certificate.getLastUpdateDate());

        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        certificate.setId(newId.longValue());

        certificate.getTags().forEach(t -> {
            Tag tag = tagRepository.save(t);
            createGiftCertificateTagRelationship(certificate.getId(), tag.getId());
        });

        return certificate;
    }

    @Override
    public GiftCertificate update(GiftCertificate entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    private void createGiftCertificateTagRelationship(Long certificateId, Long tagId) {
        jdbcTemplate.update(CREATE_CERTIFICATE_TAG_RELATIONSHIP_QUERY, certificateId, tagId);
    }

    private class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {
        @Override
        public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
            GiftCertificate cert = new GiftCertificate();
            cert.setId(rs.getLong(1));
            cert.setName(rs.getString(2));
            cert.setDescription(rs.getString(3));
            cert.setPrice(rs.getBigDecimal(4));
            cert.setDuration(rs.getInt(5));
            cert.setCreateDate(rs.getObject(6, LocalDateTime.class));
            cert.setLastUpdateDate(rs.getObject(7, LocalDateTime.class));

            List<Tag> tags = tagRepository.findByGiftCertificateId(cert.getId());
            cert.setTags(new HashSet<>(tags));
            return cert;
        }
    }
}
