package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.SessionProvider;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.RequestedPage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl extends SessionProvider implements TagRepository {

    private static final String FIND_BY_GIFT_CERTIFICATE_ID_QUERY = "SELECT g.tags FROM GiftCertificate g WHERE g.id = :id";
    private static final String FIND_BY_NAME_QUERY = "FROM Tag t WHERE t.name = :name";
    private static final String FIND_ALL_QUERY = "FROM Tag t";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM Tag t WHERE t.id = :id";

    @Override
    public Optional<Tag> findOne(Long id) {
        try {
            Tag tag = getSession().get(Tag.class, id);
            return Optional.ofNullable(tag);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> findAllPaginated(RequestedPage page) {
        Session session = getSession();
        Query<Tag> query = session.createQuery(FIND_ALL_QUERY);
        query.setFirstResult(page.getOffset());
        query.setMaxResults(page.getLimit());
        return query.getResultList();
    }

    @Override
    public Tag save(Tag entity) {
        getSession().save(entity);
        return entity;
    }

    @Override
    public Tag update(Tag entity) {
        throw new UnsupportedOperationException("Update method of a Tag entity is not supported");
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Query<Tag> query = getSession().createQuery(DELETE_BY_ID_QUERY);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Tag> findByGiftCertificateId(Long id) {
        Session session = getSession();
        Query<Tag> query = session.createQuery(FIND_BY_GIFT_CERTIFICATE_ID_QUERY);
        query.setParameter("id", id);
        return query.list();
    }

    @Override
    public Optional<Tag> findByName(String name) {
        try {
            Session session = getSession();
            Query<Tag> query = session.createQuery(FIND_BY_NAME_QUERY);
            query.setParameter("name", name);
            Tag tag = query.getSingleResult();
            return Optional.ofNullable(tag);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
