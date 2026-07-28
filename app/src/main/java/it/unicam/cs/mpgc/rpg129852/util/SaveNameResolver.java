package it.unicam.cs.mpgc.rpg129852.util;

/**
 * Resolves and determines the final, safe name for a game save.
 *
 * This component acts as an orchestrator that applies syntax validation,
 * handles fallback generation for empty inputs, and manages naming collisions
 * before persistence occurs.
 */
public interface SaveNameResolver {

    /**
     * Processes the proposed save name and returns a guaranteed valid and final save name.
     *
     * If the proposed name is null or empty, a default fallback name will be generated.
     * The method ensures that the resulting name complies with syntax rules and
     * handles collision logic based on the overwrite flag.
     *
     * @param proposedName the save name suggested by the user, which may be null or empty.
     * @param forceOverwrite true to bypass collision checks, false to prevent overwriting existing saves.
     * @return the final resolved save name ready for persistence.
     * @throws IllegalStateException if the resolved name already exists and forceOverwrite is false.
     * @throws IllegalArgumentException if the resolved name violates syntax rules.
     */
    public String resolveFinalName(String proposedName, boolean forceOverwrite);
}