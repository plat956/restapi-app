package repository;

import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TagRepository implements BaseEntityRepository<Long, Tag> {

    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM Tag WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM Tag";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
    public Boolean save(Tag entity) {
        return null;
    }

    @Override
    public Boolean update(Tag entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }
}
