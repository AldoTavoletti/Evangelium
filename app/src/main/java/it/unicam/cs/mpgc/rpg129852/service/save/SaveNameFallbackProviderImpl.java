package it.unicam.cs.mpgc.rpg129852.service.save;

import java.util.List;
import java.util.Objects;

/**
 * Concrete implementation of {@link SaveNameFallbackProvider}.
 * It generates a safe, default save name based on the pattern "untitled".
 * If the base name is already taken, it appends an incrementing counter in parentheses
 * (e.g., "untitled(1)", "untitled(2)") until it finds an available name.
 */
public class SaveNameFallbackProviderImpl implements SaveNameFallbackProvider {

    private static final String BASE_NAME = "untitled";

    @Override
    public String provideFirstAvailable(List<String> takenNames) {
        Objects.requireNonNull(takenNames, "The list of taken names must not be null.");

        if (!takenNames.contains(BASE_NAME)) {
            return BASE_NAME;
        }

        int counter = 1;
        String newName;

        do {
            newName = BASE_NAME + "(" + counter + ")";
            counter++;
        } while (takenNames.contains(newName));

        return newName;
    }
}