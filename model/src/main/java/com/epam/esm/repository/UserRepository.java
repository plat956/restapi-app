package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;

import static com.epam.esm.repository.QueryStorage.USERS_COUNT_TOP_STATISTICS;
import static com.epam.esm.repository.QueryStorage.USERS_FIND_TOP_STATISTICS;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = USERS_FIND_TOP_STATISTICS, nativeQuery = true, countQuery = USERS_COUNT_TOP_STATISTICS)
    Page<Tuple> findUserStatistics(Pageable page);
}
