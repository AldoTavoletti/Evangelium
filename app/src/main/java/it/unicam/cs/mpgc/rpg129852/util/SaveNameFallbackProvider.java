package it.unicam.cs.mpgc.rpg129852.util;

import java.util.List;

public interface SaveNameFallbackProvider {
    String provideFirstAvailable(List<String> takenNames);
}