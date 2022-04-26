package com.epam.esm.repository.impl;

import com.epam.esm.dto.TagStatisticsDto;
import com.epam.esm.dto.UserStatisticsDto;
import com.epam.esm.entity.User;
import com.epam.esm.repository.SessionProvider;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.RequestedPage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Repository
public class UserRepositoryImpl extends SessionProvider implements UserRepository {

    private static final String FIND_ALL_QUERY = "FROM User u";
    private static final String COUNT_ALL_QUERY = "SELECT count(u) FROM User u";
    private static final String FIND_TOP_USERS_STATISTICS = """
            SELECT top_users.user_id, top_users.max_amount, gt.tag_id, count(gt.tag_id) as tag_count
            FROM (
            		SELECT o.user_id as user_id, sum(o.cost) as max_amount
            		FROM orders o
            		GROUP BY o.user_id
            		HAVING sum(o.cost) = (SELECT sum(o.cost) as amount FROM orders o GROUP BY o.user_id ORDER BY amount DESC LIMIT 1)
            ) top_users
                        
            INNER JOIN orders o ON o.user_id = top_users.user_id
            INNER JOIN order_certificate oc ON oc.order_id = o.id
            INNER JOIN gift_certificate_tag gt ON gt.gift_certificate_id = oc.gift_certificate_id
                        
            INNER JOIN (
            		SELECT user_id, max(tag_count) as cnt
            		FROM (
            				SELECT user_id, max_amount, tag_id, tag_count
            				FROM (
            						SELECT top_users.user_id, top_users.max_amount, gt.tag_id, count(gt.tag_id) as tag_count
            						FROM (
            								SELECT o.user_id as user_id, sum(o.cost) as max_amount
            								FROM orders o
            								GROUP BY o.user_id
            								HAVING sum(o.cost) = (SELECT sum(o.cost) as amount FROM orders o GROUP BY o.user_id ORDER BY amount DESC LIMIT 1)
            						) top_users
            					
            						INNER JOIN orders o ON o.user_id = top_users.user_id
            						INNER JOIN order_certificate oc ON oc.order_id = o.id
            						INNER JOIN gift_certificate_tag gt ON gt.gift_certificate_id = oc.gift_certificate_id
            						GROUP BY top_users.user_id, top_users.max_amount, gt.tag_id
            				) as top_stat
            		) as top_tags
            		GROUP BY user_id
            ) as usr_max_tag_cnt
                        
            ON top_users.user_id = usr_max_tag_cnt.user_id
            GROUP BY top_users.user_id, top_users.max_amount, gt.tag_id, usr_max_tag_cnt.cnt
            HAVING count(gt.tag_id) = usr_max_tag_cnt.cnt
            """;
    private static final String COUNT_TOP_USERS_STATISTICS = "SELECT count(*) FROM (" + FIND_TOP_USERS_STATISTICS + ") stat";

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

    @Override
    public PagedModel<UserStatisticsDto> findUserStatistics(RequestedPage page) {
        Session session = getSession();
        Query query = session.createNativeQuery(FIND_TOP_USERS_STATISTICS, Tuple.class);
        query.setFirstResult(page.getOffset().intValue());
        query.setMaxResults(page.getLimit().intValue());
        List<Tuple> result = query.getResultList();
        Query<BigInteger> countQuery = getSession().createNativeQuery(COUNT_TOP_USERS_STATISTICS);
        Long totalRecords = countQuery.getSingleResult().longValue();
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(page.getLimit(), page.getPage(), totalRecords);
        return PagedModel.of(mapResultToStatisticsDtoList(result), metadata);
    }

    private Collection<UserStatisticsDto> mapResultToStatisticsDtoList(List<Tuple> result) {
        Map<Long, UserStatisticsDto> statistics = new HashMap<>();
        for(Tuple t: result) {
            Long userId = t.get("user_id", BigInteger.class).longValue();
            BigDecimal maxAmount = t.get("max_amount", BigDecimal.class);
            Long tagId = t.get("tag_id", Integer.class).longValue();
            Integer tagCount = t.get("tag_count", BigInteger.class).intValue();
            UserStatisticsDto user = statistics.getOrDefault(userId, new UserStatisticsDto(userId, maxAmount));
            TagStatisticsDto tag = new TagStatisticsDto(tagId, tagCount);
            user.getTags().add(tag);
            statistics.put(userId, user);
        }
        return statistics.values();
    }
}
