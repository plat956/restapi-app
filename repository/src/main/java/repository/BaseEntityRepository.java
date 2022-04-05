package repository;

import java.util.List;
import java.util.Optional;

public interface BaseEntityRepository<T, E> {
    Optional<E> findOne(T id);
    List<E> findAll();
    Boolean save(E entity);
    Boolean update(E entity);
    Boolean delete(T id);
}
