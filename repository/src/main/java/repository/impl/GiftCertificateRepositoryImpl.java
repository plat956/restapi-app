package repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.google.common.base.CaseFormat;
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
import util.OrderType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static repository.identity.ColumnName.*;
import static repository.identity.TableName.CERTIFICATE_TABLE_NAME;
import static util.OrderType.ASC;
import static util.OrderType.DESC;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String FIND_BY_ID_QUERY = """
        SELECT id, name, description, price, duration, create_date, last_update_date 
        FROM gift_certificate WHERE id = ?""";
    private static final String FIND_ALL_QUERY = """
        SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date 
        FROM gift_certificate gc""";
    private static final String CREATE_CERTIFICATE_TAG_RELATIONSHIP_QUERY = """
        INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)""";
    private static final String EXISTS_CERTIFICATE_TAG_RELATIONSHIP_QUERY = """
        SELECT count(*) FROM gift_certificate_tag WHERE gift_certificate_id = ? AND tag_id = ?""";
    private static final String UPDATE_QUERY = """
        UPDATE gift_certificate 
        SET name = ?, description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ?
        WHERE id = ?""";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String FIND_ALL_QUERY_TAG_PART = """
          LEFT JOIN gift_certificate_tag gct ON gc.id = gct.gift_certificate_id 
         LEFT JOIN tag t ON t.id = gct.tag_id  WHERE t.name = ?""";
    private static final String FIND_ALL_QUERY_WHERE_CLAUSE = " WHERE";
    private static final String FIND_ALL_QUERY_AND_CLAUSE = " AND";
    private static final String FIND_ALL_QUERY_ORDER_BY_CLAUSE = " ORDER BY ";
    private static final String FIND_ALL_QUERY_ORDER_BY_COLUMN_SEPARATOR = ", ";
    private static final String FIND_ALL_QUERY_SEARCH_PART = " (gc.name like ? OR gc.description like ?)";
    private static final String FIND_ALL_QUERY_GROUP_BY_TAG_ID_PART = " GROUP BY gc.id";
    private static final String FIND_ALL_QUERY_CERTIFICATE_ALIAS = "gc.";
    private static final String ORDER_TYPE_REGEX = "^(\\+|\\-).*$";
    private static final String NEGATIVE_SIGN = "-";
    private static final String COMMA_SIGN = ",";
    private static final String PERCENT_SIGN = "%";
    private static final String SPACE_SIGN = " ";

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
    public List<GiftCertificate> findAll(String tag, String search, String sort) {
        List params = new LinkedList();
        StringBuilder query = new StringBuilder(FIND_ALL_QUERY);
        if(tag != null) {
            query.append(FIND_ALL_QUERY_TAG_PART);
            params.add(tag);
        }
        if(search != null) {
            query.append(tag == null ? FIND_ALL_QUERY_WHERE_CLAUSE : FIND_ALL_QUERY_AND_CLAUSE).append(FIND_ALL_QUERY_SEARCH_PART);
            search = PERCENT_SIGN + search + PERCENT_SIGN;
            params.add(search);
            params.add(search);
        }
        if(tag != null) {
            query.append(FIND_ALL_QUERY_GROUP_BY_TAG_ID_PART);
        }
        if(sort != null) {
            query.append(buildSortingQueryPart(sort));
        }
        return jdbcTemplate.query(query.toString(), new GiftCertificateRowMapper(), params.toArray(Object[]::new));
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

    private StringBuilder buildSortingQueryPart(String sortParameter) {
        StringBuilder queryPart = new StringBuilder();
        List<String> allowedColumns = List.of("name", "createDate", "lastUpdateDate");
        OrderType orderType;
        String orderColumn;
        String[] fields = sortParameter.split(COMMA_SIGN);
        for(int i = 0; i < fields.length; i++) {
            String field = fields[i].trim();
            if(field.matches(ORDER_TYPE_REGEX)) {
                orderType = field.startsWith(NEGATIVE_SIGN) ? DESC : ASC;
                orderColumn = field.substring(1);
            } else  {
                orderType = ASC;
                orderColumn = field;
            }
            if(allowedColumns.contains(orderColumn)) {
                queryPart.append(i == 0 ? FIND_ALL_QUERY_ORDER_BY_CLAUSE : FIND_ALL_QUERY_ORDER_BY_COLUMN_SEPARATOR);
                orderColumn = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, orderColumn);
                queryPart.append(FIND_ALL_QUERY_CERTIFICATE_ALIAS).append(orderColumn).append(SPACE_SIGN).append(orderType.name());
            }
        }
        return queryPart;
    }

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
            Integer relationsCount = jdbcTemplate.queryForObject(EXISTS_CERTIFICATE_TAG_RELATIONSHIP_QUERY,
                    Integer.class, certificate.getId(), tag.getId());
            if(relationsCount == 0) {
                jdbcTemplate.update(CREATE_CERTIFICATE_TAG_RELATIONSHIP_QUERY, certificate.getId(), tag.getId());
            }
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
