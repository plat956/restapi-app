package repository;

import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TagRepository implements BaseEntityRepository<Long, Tag> {

    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM tag WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM tag";
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
        this.simpleJdbcInsert.withTableName("tag").usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Tag> findOne(Long id) {
        try {
            Tag tag = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new BeanPropertyRowMapper<>(Tag.class), id);
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
        Number newId = simpleJdbcInsert.executeAndReturnKey(Map.of("name", entity.getName()));
        entity.setId(newId.longValue());
        return entity;
    }

    @Override
    public Tag update(Tag entity) {
        throw new UnsupportedOperationException("Update method of Tag entity is not supported");
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }
}
