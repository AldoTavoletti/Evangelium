package it.unicam.cs.mpgc.rpg129852.persistence;

import it.unicam.cs.mpgc.rpg129852.dto.Resource;

import java.util.List;

/**
 * Defines the contract for a generic registry that loads, stores, and retrieves
 * read-only game resources (e.g., books, levels, scriptures).
 *
 * @param <T> the specific type of resource managed by this registry, which must extend {@link Resource}
 */
public interface ResourceRegistry<T extends Resource> {

    /**
     * Triggers the loading process, populating the internal registry
     * from the underlying data source (e.g., a file or a database).
     *
     * @throws RuntimeException if the loading process fails
     */
    void loadResources();

    /**
     * Retrieves all currently loaded resources.
     *
     * @return an unmodifiable list of all resources
     */
    List<T> getAllResources();

    /**
     * Retrieves a specific resource by its unique identifier.
     *
     * @param id the unique string identifier of the requested resource
     * @return the resource associated with the provided ID
     * @throws IllegalArgumentException if no resource matches the provided ID
     * @throws NullPointerException if the provided ID is null
     */
    T getResource(String id);
}