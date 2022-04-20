package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.OrderType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String ORDER_TYPE_REGEX = "^(\\+|\\-).*$";
    private static final String NEGATIVE_SIGN = "-";
    private static final String COMMA_SIGN = ",";
    private static final String PERCENT_SIGN = "%";

    private SessionFactory sessionFactory;
    private TagRepository tagRepository;

    @Autowired
    public GiftCertificateRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

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
    public List<GiftCertificate> findAll(String tag, String search, String sort) {
        Session session = getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certRoot = criteriaQuery.from(GiftCertificate.class);

        criteriaQuery.select(certRoot);

        if (tag != null) {
            Join<GiftCertificate, Tag> tagJoin = certRoot.join("tags", JoinType.LEFT);
            criteriaQuery.where(criteriaBuilder.equal(tagJoin.get("name"), tag));
        }

        if (search != null) {
            search = PERCENT_SIGN + search + PERCENT_SIGN;
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(certRoot.get("name"), search),
                    criteriaBuilder.like(certRoot.get("description"), search)
            );
            criteriaQuery.where(searchPredicate);
        }

        if (sort != null) {
            List<Order> orders = buildCertificatesOrdering(criteriaBuilder, certRoot, sort);
            criteriaQuery.orderBy(orders);
        }

        criteriaQuery.distinct(true);

        Query<GiftCertificate> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    @Transactional
    public GiftCertificate save(GiftCertificate certificate) {
        Session session = getSession();
        saveTags(certificate);
        session.save(certificate);
        return certificate;
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate certificate) {
        Session session = getSession();
        saveTags(certificate);
        session.update(certificate);
        return certificate;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Session session = getSession();
        GiftCertificate certificate = session.load(GiftCertificate.class, id);
        session.delete(certificate);
    }

    private List<Order> buildCertificatesOrdering(CriteriaBuilder criteriaBuilder, Root<GiftCertificate> certRoot, String sortParam) {
        List<Order> orderList = new ArrayList<>();
        List<String> allowedColumns = List.of("name", "createDate", "lastUpdateDate");
        OrderType orderType;
        String orderColumn;
        String[] fields = sortParam.split(COMMA_SIGN);
        for (String f: fields) {
            String field = f.trim();
            if (field.matches(ORDER_TYPE_REGEX)) {
                orderType = field.startsWith(NEGATIVE_SIGN) ? DESC : ASC;
                orderColumn = field.substring(1);
            } else {
                orderType = ASC;
                orderColumn = field;
            }
            if (allowedColumns.contains(orderColumn)) {
                Order order;
                Path column = certRoot.get(orderColumn);
                if (orderType == ASC) {
                    order = criteriaBuilder.asc(column);
                } else {
                    order = criteriaBuilder.desc(column);
                }
                orderList.add(order);
            }
        }
        return orderList;
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

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
