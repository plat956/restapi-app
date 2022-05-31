package com.epam.esm.mapper;

import com.epam.esm.exception.EntityMappingException;

/**
 * The Entity mapper interface.
 *
 * @param <T> the DTO parameter
 * @param <E> the Entity object parameter
 */
public interface EntityMapper<T, E> {

    /**
     * Map DTO to Entity object
     *
     * @param dto the DTO
     * @return the mapped Entity object
     * @throws EntityMappingException the entity mapping exception if the dto contains wrong user or certificates ids
     */
    E toEntity(T dto) throws EntityMappingException;

    /**
     * Map Entity object to DTO
     *
     * @param entity the Entity object
     * @return the mapped DTO
     */
    T toDto(E entity);
}
