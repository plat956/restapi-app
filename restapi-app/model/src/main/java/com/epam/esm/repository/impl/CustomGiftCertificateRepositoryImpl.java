package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.CustomGiftCertificateRepository;
import com.epam.esm.util.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.util.OrderType.ASC;
import static com.epam.esm.util.OrderType.DESC;

@Repository
public class CustomGiftCertificateRepositoryImpl implements CustomGiftCertificateRepository {

    private static final String ORDER_TYPE_REGEX = "^(\\+|\\-).*$";
    private static final String NEGATIVE_SIGN = "-";
    private static final String PERCENT_SIGN = "%";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<GiftCertificate> findAll(List<String> tags, String search, List<String> sorts, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certRoot = criteriaQuery.from(GiftCertificate.class);

        criteriaQuery.select(certRoot);

        fillFindAllQueryWithParams(tags, search, sorts, criteriaBuilder, criteriaQuery, certRoot);

        Query query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<GiftCertificate> certificates = query.getResultList();
        Long totalRecords = getTotalRecords(tags, search, sorts);

        return new PageImpl<>(certificates, pageable, totalRecords);
    }

    private Long getTotalRecords(List<String> tags, String search, List<String> sort) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<GiftCertificate> cr = cq.from(GiftCertificate.class);
        cq.select(cr.get("id"));

        fillFindAllQueryWithParams(tags, search, sort, cb, cq, cr);
        cq.orderBy(List.of());
        try {
            return (long) entityManager.createQuery(cq).getResultList().size();
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
}
