package it.unicam.cs.mpgc.rpg129852.context.level;

import it.unicam.cs.mpgc.rpg129852.model.level.Level;

/**
 * Fornisce l'accesso in sola lettura al livello attualmente in esecuzione.
 */
public interface LevelProvider {
    Level getCurrentLevel();
    boolean hasActiveLevel();
}