package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserStatisticsDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceException;
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
     * @return the optional with a user object if it exists, otherwise the empty one
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


    /**
     * Find user by login.
     *
     * @param login the user login
     * @return the optional with a user object if it exists, otherwise the empty one
     */
    Optional<User> findByLogin(String login);

    /**
     * Sign up a new user.
     *
     * @param userDto the user dto with new user data
     * @return the user object represents the registered user
     * @throws ServiceException the service exception if a user with the passed login already exists
     */
    User signUp(UserDto userDto) throws ServiceException;

    /**
     * Change a user status.
     *
     * @param userId the user id
     * @param status the new status for changing
     * @return the user object represents the updated user
     */
    User changeStatus(Long userId, User.Status status) throws ServiceException;
}
