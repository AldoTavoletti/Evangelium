package it.unicam.cs.mpgc.rpg129852.util;

import it.unicam.cs.mpgc.rpg129852.persistence.AvailableSavesProvider;

import java.util.List;

public class SaveNameManagerImpl implements SaveNameManager {

    private final AvailableSavesProvider savesProvider;
    private final NameValidator nameValidator;
    private final SaveNameGenerator nameGenerator;

    public SaveNameManagerImpl(AvailableSavesProvider savesProvider, NameValidator nameValidator, SaveNameGenerator nameGenerator) {
        this.savesProvider = savesProvider;
        this.nameValidator = nameValidator;
        this.nameGenerator = nameGenerator;
    }

    @Override
    public String resolveAndValidate(String requestedName, boolean forceOverwrite) {
        // Leggiamo il disco una volta sola
        List<String> existingSaves = savesProvider.getAvailableSaves();

        // 1. Risoluzione
        String finalName = resolveName(requestedName, existingSaves);

        // 2. Validazione
        validateName(finalName, existingSaves, forceOverwrite);

        return finalName;
    }

    private String resolveName(String requestedName, List<String> existingSaves) {
        if (requestedName == null || requestedName.trim().isEmpty()) {
            return nameGenerator.generate(existingSaves);
        }
        return requestedName;
    }

    private void validateName(String name, List<String> existingSaves, boolean forceOverwrite) {
        // Validazione sintattica
        nameValidator.validate(name);

        // Validazione semantica
        if (!forceOverwrite && existingSaves.contains(name)) {
            throw new IllegalStateException("A saving with the same name already exists.");
        }
    }
}