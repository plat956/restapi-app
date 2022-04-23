package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.SessionProvider;
import com.epam.esm.util.RequestedPage;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl extends SessionProvider implements OrderRepository {

    private static final String FIND_BY_USER_ID_QUERY  = "FROM Order o WHERE o.user.id = :id";

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
    public List<Order> findAllPaginated(RequestedPage page) {
        throw new UnsupportedOperationException("FindAllPaginated method of an Order entity is not supported");
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
    public List<Order> findByUserIdPaginated(Long id, RequestedPage page) {
        Query<Order> query = getSession().createQuery(FIND_BY_USER_ID_QUERY);
        query.setParameter("id", id);
        query.setFirstResult(page.getOffset());
        query.setMaxResults(page.getLimit());
        return query.getResultList();
    }
}
