package repository.impl;

import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import repository.TagRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static repository.identity.ColumnName.ENTITY_ID;
import static repository.identity.ColumnName.TAG_NAME;
import static repository.identity.TableName.TAG_TABLE_NAME;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM tag WHERE id = ?";
    private static final String FIND_BY_NAME = "SELECT id, name FROM tag WHERE name = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM tag";
    private static final String FIND_BY_GIFT_CERTIFICATE_ID = """
            SELECT t.id, t.name FROM tag t
            JOIN gift_certificate_tag gct 
            ON t.id = gct.tag_id 
            WHERE gct.gift_certificate_id = ?""";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM tag WHERE id = ?";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setSimpleJdbcInsert(SimpleJdbcInsert simpleJdbcInsert) {
        this.simpleJdbcInsert = simpleJdbcInsert;
        this.simpleJdbcInsert.withTableName(TAG_TABLE_NAME).usingGeneratedKeyColumns(ENTITY_ID);
    }

    @Override
    public Optional<Tag> findOne(Long id) {
        try {
            Tag tag = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY,
                    new BeanPropertyRowMapper<>(Tag.class), id);
            return Optional.of(tag);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Tag save(Tag entity) {
        Number newId = simpleJdbcInsert.executeAndReturnKey(Map.of(TAG_NAME, entity.getName()));
        entity.setId(newId.longValue());
        return entity;
    }

    @Override
    public Tag update(Tag entity) {
        throw new UnsupportedOperationException("Update method of a Tag entity is not supported");
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }

    @Override
    public List<Tag> findByGiftCertificateId(Long id) {
        return jdbcTemplate.query(FIND_BY_GIFT_CERTIFICATE_ID, new BeanPropertyRowMapper<>(Tag.class), id);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        try {
            Tag tag = jdbcTemplate.queryForObject(FIND_BY_NAME, new BeanPropertyRowMapper<>(Tag.class), name);
            return Optional.of(tag);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
