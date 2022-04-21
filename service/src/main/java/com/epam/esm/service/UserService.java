package com.epam.esm.service;

import com.epam.esm.entity.User;

import java.util.List;
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
    Optional<User> findOne(Long id);

    /**
     * Find all users.
     *
     * @return the list of users or empty one
     */
    List<User> findAll();
}
