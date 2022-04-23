package com.epam.esm.repository;

import com.epam.esm.entity.BaseEntity;
import com.epam.esm.util.RequestedPage;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, E extends BaseEntity> {

    Optional<E> findOne(T id);
    List<E> findAllPaginated(RequestedPage page);
    E save(E entity);
    E update(E entity);
    void delete(T id);
}
