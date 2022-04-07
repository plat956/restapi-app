package repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import repository.GiftCertificateRepository;
import repository.TagRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import static repository.impl.IdentityStorage.*;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String FIND_BY_ID_QUERY = """
        SELECT id, name, description, price, duration, create_date, last_update_date 
        FROM gift_certificate WHERE id = ?""";
    private static final String FIND_ALL_QUERY = """
        SELECT id, name, description, price, duration, create_date, last_update_date 
        FROM gift_certificate""";
    private static final String CREATE_CERTIFICATE_TAG_RELATIONSHIP_QUERY = """
        INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?) 
        ON CONFLICT DO NOTHING""";
    private static final String UPDATE_QUERY = """
        UPDATE gift_certificate 
        SET name = ?, description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ?
        WHERE id = ?""";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM gift_certificate WHERE id = ?";

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
        this.simpleJdbcInsert.withTableName(CERTIFICATE_TABLE_NAME).usingGeneratedKeyColumns(ENTITY_ID);
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
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(CERTIFICATE_NAME, certificate.getName())
                .addValue(CERTIFICATE_DESCRIPTION, certificate.getDescription())
                .addValue(CERTIFICATE_PRICE, certificate.getPrice())
                .addValue(CERTIFICATE_DURATION, certificate.getDuration())
                .addValue(CERTIFICATE_CREATE_DATE, certificate.getCreateDate())
                .addValue(CERTIFICATE_LAST_UPDATE_DATE, certificate.getLastUpdateDate());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        certificate.setId(newId.longValue());
        updateTags(certificate);
        return certificate;
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate certificate) {
        jdbcTemplate.update(UPDATE_QUERY,
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate(),
                certificate.getId()
        );
        updateTags(certificate);

        List<Tag> tags = tagRepository.findByGiftCertificateId(certificate.getId());
        certificate.setTags(new HashSet<>(tags));
        return certificate;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }

    //todo transactional? find it out
    private void updateTags(GiftCertificate certificate) {
        certificate.getTags().forEach(t -> {
            Optional<Tag> existingTag = tagRepository.findByName(t.getName());
            Tag tag;
            if(existingTag.isPresent()) {
                tag = existingTag.get();
            } else {
                tag = tagRepository.save(t);
            }
            t.setId(tag.getId());
            jdbcTemplate.update(CREATE_CERTIFICATE_TAG_RELATIONSHIP_QUERY, certificate.getId(), tag.getId());
        });
    }

    private class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {
        @Override
        public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
            GiftCertificate cert = new GiftCertificate();
            cert.setId(rs.getLong(ENTITY_ID));
            cert.setName(rs.getString(CERTIFICATE_NAME));
            cert.setDescription(rs.getString(CERTIFICATE_DESCRIPTION));
            cert.setPrice(rs.getBigDecimal(CERTIFICATE_PRICE));
            cert.setDuration(rs.getInt(CERTIFICATE_DURATION));
            cert.setCreateDate(rs.getObject(CERTIFICATE_CREATE_DATE, LocalDateTime.class));
            cert.setLastUpdateDate(rs.getObject(CERTIFICATE_LAST_UPDATE_DATE, LocalDateTime.class));

            List<Tag> tags = tagRepository.findByGiftCertificateId(cert.getId());
            cert.setTags(new HashSet<>(tags));
            return cert;
        }
    }
}
