package com.epam.esm.repository.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.SessionProvider;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.RequestedPage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends SessionProvider implements UserRepository {

    private static final String FIND_ALL_QUERY = "FROM User u";
    private static final String COUNT_ALL_QUERY = "SELECT count(u) FROM User u";

    @Override
    public Optional<User> findOne(Long id) {
        try {
            Session session = getSession();
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public PagedModel<User> findAllPaginated(RequestedPage page) {
        Session session = getSession();
        Query<User> query = session.createQuery(FIND_ALL_QUERY);
        query.setFirstResult(page.getOffset().intValue());
        query.setMaxResults(page.getLimit().intValue());
        List<User> users = query.getResultList();

        Query<Long> countQuery = getSession().createQuery(COUNT_ALL_QUERY);
        Long totalRecords = countQuery.getSingleResult();

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(page.getLimit(), page.getPage(), totalRecords);
        return PagedModel.of(users, metadata);
    }

    @Override
    public User save(User entity) {
        throw new UnsupportedOperationException("Save method of a User entity is not supported");
    }

    @Override
    public User update(User entity) {
        throw new UnsupportedOperationException("Update method of a User entity is not supported");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Delete method of a User entity is not supported");
    }
}
