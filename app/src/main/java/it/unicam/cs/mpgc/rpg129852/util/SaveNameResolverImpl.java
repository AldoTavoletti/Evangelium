package it.unicam.cs.mpgc.rpg129852.util;

import it.unicam.cs.mpgc.rpg129852.persistence.AvailableSavesProvider;

import java.util.List;

public class SaveNameResolverImpl implements SaveNameResolver {

    private final AvailableSavesProvider savesProvider;
    private final SyntaxValidator syntaxValidator;
    private final SaveNameFallbackProvider fallbackProvider;

    public SaveNameResolverImpl(AvailableSavesProvider savesProvider, SyntaxValidator syntaxValidator, SaveNameFallbackProvider fallbackProvider) {
        this.savesProvider = savesProvider;
        this.syntaxValidator = syntaxValidator;
        this.fallbackProvider = fallbackProvider;
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
            throw new IllegalStateException("A saving with the same name already exists.");
        }
    }
}