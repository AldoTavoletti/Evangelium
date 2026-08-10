package it.unicam.cs.mpgc.rpg129852.service.save;

import it.unicam.cs.mpgc.rpg129852.persistence.game.AvailableSavesProvider;
import it.unicam.cs.mpgc.rpg129852.util.SyntaxValidator;

import java.util.List;
import java.util.Objects;

/**
 * Concrete implementation of {@link SaveNameResolver}.
 * It orchestrates the process of determining a valid save name by applying
 * a fallback mechanism if the proposed name is empty, and subsequently
 * running syntactic and semantic (duplication) validations.
 */
public class SaveNameResolverImpl implements SaveNameResolver {

    private final AvailableSavesProvider savesProvider;
    private final SyntaxValidator syntaxValidator;
    private final SaveNameFallbackProvider fallbackProvider;

    /**
     * Constructs a new save name resolver.
     *
     * @param savesProvider    the provider used to retrieve currently taken save names
     * @param syntaxValidator  the validator ensuring the name contains no illegal characters
     * @param fallbackProvider the provider used to generate a name if the proposed one is blank
     * @throws NullPointerException if any of the dependencies are null
     */
    public SaveNameResolverImpl(AvailableSavesProvider savesProvider, SyntaxValidator syntaxValidator, SaveNameFallbackProvider fallbackProvider) {
        this.savesProvider = Objects.requireNonNull(savesProvider, "The saves provider must not be null.");
        this.syntaxValidator = Objects.requireNonNull(syntaxValidator, "The syntax validator must not be null.");
        this.fallbackProvider = Objects.requireNonNull(fallbackProvider, "The fallback provider must not be null.");
    }

    @Override
    public String resolveFinalName(String proposedName, boolean forceOverwrite) {
        List<String> takenNames = savesProvider.getAvailableSaves();

        String nameToValidate = applyFallbackIfEmpty(proposedName, takenNames);
        verifyNameIsAllowed(nameToValidate, takenNames, forceOverwrite);

        return nameToValidate;
    }

    private String applyFallbackIfEmpty(String proposedName, List<String> takenNames) {
        if (proposedName == null || proposedName.trim().isEmpty()) {
            return fallbackProvider.provideFirstAvailable(takenNames);
        }
        return proposedName;
    }

    private void verifyNameIsAllowed(String name, List<String> takenNames, boolean forceOverwrite) {
        // syntax validation
        syntaxValidator.validate(name);

        // semantic validation
        if (!forceOverwrite && takenNames.contains(name)) {
            throw new SaveAlreadyExistsException("A saving with the same name already exists.");
        }
    }
}