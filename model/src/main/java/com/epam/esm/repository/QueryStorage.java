package com.epam.esm.repository;

final class QueryStorage {

    //Certificates
    static final String CERTIFICATES_FIND_BY_ORDER_ID = """
                SELECT g.id, g.operation, g.timestamp, g.create_date, g.description, g.duration, g.last_update_date, g.name, g.price 
                FROM orders o 
                INNER JOIN order_certificate oc on o.id = oc.order_id 
                INNER JOIN gift_certificates g on oc.gift_certificate_id = g.id 
                WHERE o.id = :id
            """;

    static final String CERTIFICATES_COUNT_BY_ORDER_ID = "SELECT count(id) FROM (" + CERTIFICATES_FIND_BY_ORDER_ID + ") cnt";

    //Users
    static final String USERS_FIND_TOP_STATISTICS = """
            SELECT top_user.user_id, top_user.max_amount, gt.tag_id, count(gt.tag_id) as tag_count
            FROM (
                    SELECT user_id, max(tag_count) as max_tag_count, max_amount
                    FROM (

                            SELECT top_user_amount.user_id, top_user_amount.max_amount, gt.tag_id, count(gt.tag_id) as tag_count
                            FROM (
                                    SELECT o.user_id, sum(o.cost) as max_amount
                                    FROM orders o
                                    GROUP BY o.user_id
                                    HAVING sum(o.cost) = (SELECT sum(o.cost) as amount FROM orders o GROUP BY o.user_id ORDER BY amount DESC LIMIT 1)
                            ) top_user_amount

                            INNER JOIN orders o ON o.user_id = top_user_amount.user_id
                            INNER JOIN order_certificate oc ON oc.order_id = o.id
                            INNER JOIN gift_certificate_tag gt ON gt.gift_certificate_id = oc.gift_certificate_id
                            GROUP BY top_user_amount.user_id, top_user_amount.max_amount, gt.tag_id

                    ) as top_user_tag GROUP BY user_id, max_amount
            ) as top_user

            INNER JOIN orders o ON o.user_id = top_user.user_id
            INNER JOIN order_certificate oc ON oc.order_id = o.id
            INNER JOIN gift_certificate_tag gt ON gt.gift_certificate_id = oc.gift_certificate_id

            GROUP BY top_user.user_id, top_user.max_amount, gt.tag_id, top_user.max_tag_count
            HAVING count(gt.tag_id) = top_user.max_tag_count
        """;

    static final String USERS_COUNT_TOP_STATISTICS = "SELECT count(user_id) FROM (" + USERS_FIND_TOP_STATISTICS + ") stat";

    private QueryStorage() {
    }
}
