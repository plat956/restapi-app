package com.epam.esm.service;

import com.epam.esm.dto.UserStatisticsDto;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * The User service interface.
 * Specifies a set of methods to get and manage users
 */
public interface UserService {
    /**
     * Find one user.
     *
     * @param id the user id
     * @return the optional with a user object if it exists, otherwise the empty optional
     */
    Optional<User> findById(Long id);

    /**
     * Find all users.
     *
     * @param pageable object containing page and size request parameters
     * @return the page object with users or empty one
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Find users by highest cost of all orders and their tag statistics.
     *
     * @param pageable object containing page and size request parameters
     * @return the page object with top users
     */
    Page<UserStatisticsDto> findUserStatistics(Pageable pageable);
}
