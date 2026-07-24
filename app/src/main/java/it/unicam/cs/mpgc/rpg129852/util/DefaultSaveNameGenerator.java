package it.unicam.cs.mpgc.rpg129852.util;

import java.util.List;

public class DefaultSaveNameGenerator implements SaveNameGenerator {

    private static final String BASE_NAME = "untitled";

    @Override
    public String generate(List<String> existingSaves) {
        if (!existingSaves.contains(BASE_NAME)) {
            return BASE_NAME;
        }

        int counter = 1;
        String newName;

        do {
            newName = BASE_NAME + "(" + counter + ")";
            counter++;
        } while (existingSaves.contains(newName));

        return newName;
    }
}