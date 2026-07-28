package it.unicam.cs.mpgc.rpg129852.persistence;

import java.util.List;

/**
 * Provides read-only access to the collection of existing game saves.
 *
 * This interface is isolated to comply with the Interface Segregation Principle,
 * allowing components (like name resolvers or UI controllers) to query existing
 * saves without requiring full read/write repository privileges.
 */
public interface AvailableSavesProvider {

    /**
     * Retrieves a list of all currently available save names.
     *
     * @return a list of save names (excluding file extensions or internal paths),
     *         or an empty list if no saves are found.
     */
    List<String> getAvailableSaves();
}