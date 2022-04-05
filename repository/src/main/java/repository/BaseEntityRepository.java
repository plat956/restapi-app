package repository;

import java.util.Optional;

public interface BaseEntityRepository<T, E> {
    Boolean create(E entity);
    Optional<E> findById(T id);
    Boolean update(E entity);
    Boolean deleteById(T id);
}
