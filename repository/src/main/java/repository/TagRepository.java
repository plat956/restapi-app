package repository;

import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TagRepository implements BaseEntityRepository<Long, Tag> {

    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM Tag WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Boolean create(Tag entity) {
        return null;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        Tag tag = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new BeanPropertyRowMapper<>(Tag.class), id);
        return Optional.ofNullable(tag);
    }

    @Override
    public Boolean update(Tag entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Boolean deleteById(Long id) {
        return null;
    }
}
