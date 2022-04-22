package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.SessionProvider;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.OrderType;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.util.OrderType.ASC;
import static com.epam.esm.util.OrderType.DESC;

@Repository
public class GiftCertificateRepositoryImpl extends SessionProvider implements GiftCertificateRepository {

    private static final String ORDER_TYPE_REGEX = "^(\\+|\\-).*$";
    private static final String NEGATIVE_SIGN = "-";
    private static final String PERCENT_SIGN = "%";
    private static final String FIND_BY_ID_IN_QUERY = "FROM GiftCertificate WHERE id in(:ids)";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM GiftCertificate t WHERE t.id = :id";

    private TagRepository tagRepository;

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Optional<GiftCertificate> findOne(Long id) {
        try {
            GiftCertificate certificate = getSession().get(GiftCertificate.class, id);
            return Optional.ofNullable(certificate);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<GiftCertificate> findAll() {
        Query query = getSession().createQuery("FROM GiftCertificate g");
        return query.getResultList();
    }

    @Override
    @Transactional
    public GiftCertificate save(GiftCertificate certificate) {
        saveTags(certificate);
        getSession().save(certificate);
        return certificate;
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate certificate) {
        saveTags(certificate);
        getSession().update(certificate);
        return certificate;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Query<GiftCertificate> query = getSession().createQuery(DELETE_BY_ID_QUERY);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<GiftCertificate> findAll(List<String> tags, String search, List<String> sort) {
        Session session = getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certRoot = criteriaQuery.from(GiftCertificate.class);

        criteriaQuery.select(certRoot);

        if (tags != null && !tags.isEmpty()) {
            addFilteringByTags(criteriaBuilder, criteriaQuery, certRoot, tags);
        }
        if (search != null) {
            addSearch(criteriaBuilder, criteriaQuery, certRoot, search);
        }
        if (sort != null && !sort.isEmpty()) {
            addOrdering(criteriaBuilder, criteriaQuery, certRoot, sort);
        }
        Query<GiftCertificate> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public List<GiftCertificate> findByIdIn(List<Long> ids) {
        Query<GiftCertificate> query = getSession().createQuery(FIND_BY_ID_IN_QUERY);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    private void addOrdering(CriteriaBuilder cb, CriteriaQuery cq, Root<GiftCertificate> certRoot, List<String> sort) {
        List<Order> orderList = new ArrayList<>();
        List<String> allowedColumns = List.of("name", "createDate", "lastUpdateDate");
        OrderType orderType;
        String orderColumn;
        for (String f: sort) {
            String field = f.trim();
            if (field.matches(ORDER_TYPE_REGEX)) {
                orderType = field.startsWith(NEGATIVE_SIGN) ? DESC : ASC;
                orderColumn = field.substring(1);
            } else {
                orderType = ASC;
                orderColumn = field;
            }
            if (allowedColumns.contains(orderColumn)) {
                Path column = certRoot.get(orderColumn);
                Order order = orderType == ASC ? cb.asc(column) : cb.desc(column);
                orderList.add(order);
            }
        }
        cq.orderBy(orderList);
    }

    private void addFilteringByTags(CriteriaBuilder cb, CriteriaQuery cq, Root<GiftCertificate> certRoot, List<String> tags) {
        Join<GiftCertificate, Tag> tagJoin = certRoot.join("tags", JoinType.LEFT);
        Predicate[] tagPredicates = new Predicate[tags.size()];

        for(int i = 0; i < tags.size(); i++) {
            Predicate p = cb.equal(tagJoin.get("name"), tags.get(i));
            tagPredicates[i] = p;
        }

        cq.where(cb.or(tagPredicates));
        cq.groupBy(certRoot.get("id"));
        cq.having(
                cb.equal(
                        cb.count(certRoot.get("id")),
                        tags.size()
                )
        );
    }

    private void addSearch(CriteriaBuilder cb, CriteriaQuery cq, Root<GiftCertificate> certRoot, String search) {
        search = PERCENT_SIGN + search + PERCENT_SIGN;
        Predicate searchPredicate = cb.or(
                cb.like(certRoot.get("name"), search),
                cb.like(certRoot.get("description"), search)
        );
        cq.where(searchPredicate);
    }

    private void saveTags(GiftCertificate certificate) {
        certificate.getTags().forEach(t -> {
            Optional<Tag> existingTag = tagRepository.findByName(t.getName());
            if(existingTag.isPresent()) {
                t.setId(existingTag.get().getId());
            } else {
                tagRepository.save(t);
            }
        });
    }
}
