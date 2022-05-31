package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.Optional;

import static com.epam.esm.repository.QueryStorage.USERS_COUNT_TOP_STATISTICS;
import static com.epam.esm.repository.QueryStorage.USERS_FIND_TOP_STATISTICS;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = USERS_FIND_TOP_STATISTICS,
            countQuery = USERS_COUNT_TOP_STATISTICS,
            nativeQuery = true)
    Page<Tuple> findUserStatistics(Pageable page);

    Optional<User> findByLogin(String login);

    Boolean existsByLogin(String login);
}
