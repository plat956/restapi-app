package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.SessionProvider;
import com.epam.esm.util.RequestedPage;
import org.hibernate.query.Query;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl extends SessionProvider implements OrderRepository {

    private static final String FIND_BY_USER_ID_QUERY = "FROM Order o WHERE o.user.id = :id";
    private static final String COUNT_BY_USER_ID_QUERY = "SELECT count(o) FROM Order o WHERE o.user.id = :id";

    @Override
    public Optional<Order> findOne(Long id) {
        try {
            Order order = getSession().get(Order.class, id);
            return Optional.ofNullable(order);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public PagedModel<Order> findAllPaginated(RequestedPage page) {
        throw new UnsupportedOperationException("findAllPaginated method of an Order entity is not supported");
    }

    @Override
    @Transactional
    public Order save(Order order) {
        getSession().save(order);
        return order;
    }

    @Override
    public Order update(Order order) {
        throw new UnsupportedOperationException("Update method of an Order entity is not supported");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Delete method of an Order entity is not supported");
    }

    @Override
    public PagedModel<Order> findByUserIdPaginated(Long id, RequestedPage page) {
        Query<Order> query = getSession().createQuery(FIND_BY_USER_ID_QUERY);
        query.setParameter("id", id);
        query.setFirstResult(page.getOffset().intValue());
        query.setMaxResults(page.getLimit().intValue());
        List<Order> orders = query.getResultList();

        Query<Long> countQuery = getSession().createQuery(COUNT_BY_USER_ID_QUERY);
        countQuery.setParameter("id", id);
        Long totalRecords = countQuery.getSingleResult();

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(page.getLimit(), page.getPage(), totalRecords);
        return PagedModel.of(orders, metadata);
    }
}
