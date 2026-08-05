package it.unicam.cs.mpgc.rpg129852.service.save;

import java.util.List;

/**
 * Provides an automated fallback mechanism for generating valid save names
 * when the user input is absent or invalid.
 */
public interface SaveNameFallbackProvider {

    /**
     * Generates the first available fallback name that does not collide
     * with any of the currently taken names.
     *
     * The implementation typically relies on a base name (e.g., "untitled")
     * and increments a counter until a unique name is found.
     *
     * @param takenNames a list of save names currently in use.
     * @return a unique fallback name guaranteed not to be present in the provided list.
     */
    String provideFirstAvailable(List<String> takenNames);
}