package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.SessionProvider;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.OrderType;
import com.epam.esm.util.RequestedPage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
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
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM GiftCertificate t WHERE t.id = :id";
    private static final String FIND_ALL_QUERY = "FROM GiftCertificate g";
    private static final String COUNT_ALL_QUERY = "SELECT count(g) FROM GiftCertificate g";
    private static final String FIND_BY_ORDER_ID = "SELECT o.giftCertificates FROM Order o WHERE o.id = :id";
    private static final String COUNT_BY_ORDER_ID = "SELECT count(c.id) FROM Order o INNER JOIN o.giftCertificates c where o.id = :id";

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
    public PagedModel<GiftCertificate> findAllPaginated(RequestedPage page) {
        Query query = getSession().createQuery(FIND_ALL_QUERY);
        List<GiftCertificate> certificates = query.getResultList();

        Query<Long> countQuery = getSession().createQuery(COUNT_ALL_QUERY);
        Long totalRecords = countQuery.getSingleResult();

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(page.getLimit(), page.getPage(), totalRecords);
        return PagedModel.of(certificates, metadata);
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
    public PagedModel<GiftCertificate> findAllPaginated(List<String> tags, String search, List<String> sort, RequestedPage page) {
        Session session = getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certRoot = criteriaQuery.from(GiftCertificate.class);

        criteriaQuery.select(certRoot);

        fillFindAllQueryWithParams(tags, search, sort, criteriaBuilder, criteriaQuery, certRoot);

        Query<GiftCertificate> query = session.createQuery(criteriaQuery);
        query.setFirstResult(page.getOffset().intValue());
        query.setMaxResults(page.getLimit().intValue());

        List<GiftCertificate> certificates = query.getResultList();
        Long totalRecords = getTotalRecords(tags, search, sort);

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(page.getLimit(), page.getPage(), totalRecords);
        return PagedModel.of(certificates, metadata);
    }

    @Override
    public PagedModel<GiftCertificate> findByOrderIdPaginated(Long id, RequestedPage page) {
        Query<GiftCertificate> query = getSession().createQuery(FIND_BY_ORDER_ID);
        query.setParameter("id", id);
        query.setFirstResult(page.getOffset().intValue());
        query.setMaxResults(page.getLimit().intValue());
        List<GiftCertificate> certificates = query.getResultList();

        Query<Long> countQuery = getSession().createQuery(COUNT_BY_ORDER_ID);
        countQuery.setParameter("id", id);
        Long totalRecords = countQuery.getSingleResult();

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(page.getLimit(), page.getPage(), totalRecords);
        return PagedModel.of(certificates, metadata);
    }

    private Long getTotalRecords(List<String> tags, String search, List<String> sort) {
        Session session = getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<GiftCertificate> cr = cq.from(GiftCertificate.class);
        cq.select(cb.count(cr));

        fillFindAllQueryWithParams(tags, search, sort, cb, cq, cr);
        cq.orderBy(List.of());
        try {
            return session.createQuery(cq).getSingleResult();
        } catch (NoResultException ex) {
            return 0L;
        }
    }

    private void fillFindAllQueryWithParams(List<String> tags, String search, List<String> sort,
                           CriteriaBuilder cb, CriteriaQuery cq, Root certRoot) {
        List<Predicate> wherePredicates = new ArrayList<>();

        if (tags != null && !tags.isEmpty()) {
            addFilteringByTags(cb, cq, certRoot, tags, wherePredicates);
        }
        if (search != null) {
            addSearch(cb, certRoot, search, wherePredicates);
        }

        cq.where(wherePredicates.toArray(Predicate[]::new));

        if (sort != null && !sort.isEmpty()) {
            addOrdering(cb, cq, certRoot, sort);
        }
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

    private void addFilteringByTags(CriteriaBuilder cb, CriteriaQuery cq, Root<GiftCertificate> certRoot,
                                    List<String> tags, List<Predicate> wherePredicates) {
        Join<GiftCertificate, Tag> tagJoin = certRoot.join("tags", JoinType.LEFT);
        Predicate[] tagPredicates = new Predicate[tags.size()];

        for(int i = 0; i < tags.size(); i++) {
            Predicate p = cb.equal(tagJoin.get("name"), tags.get(i));
            tagPredicates[i] = p;
        }

        wherePredicates.add(cb.or(tagPredicates));

        cq.groupBy(certRoot.get("id"));
        cq.having(
                cb.equal(
                        cb.count(certRoot.get("id")),
                        tags.size()
                )
        );
    }

    private void addSearch(CriteriaBuilder cb, Root<GiftCertificate> certRoot,
                           String search, List<Predicate> wherePredicates) {
        search = PERCENT_SIGN + search.toLowerCase() + PERCENT_SIGN;
        Predicate searchPredicate = cb.or(
                cb.like(cb.lower(certRoot.get("name")), search),
                cb.like(cb.lower(certRoot.get("description")), search)
        );
        wherePredicates.add(searchPredicate);
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
