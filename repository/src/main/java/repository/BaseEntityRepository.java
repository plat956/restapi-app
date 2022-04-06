package repository;

import java.util.List;
import java.util.Optional;

public interface BaseEntityRepository<T, E> {
    Optional<E> findOne(T id);
    List<E> findAll();
    E save(E entity);
    E update(E entity);
    void delete(T id);
}
