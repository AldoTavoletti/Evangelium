package it.unicam.cs.mpgc.rpg129852.service.save;

import java.util.List;

public class SaveNameFallbackProviderImpl implements SaveNameFallbackProvider {

    private static final String BASE_NAME = "untitled";

    @Override
    public String provideFirstAvailable(List<String> takenNames) {
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