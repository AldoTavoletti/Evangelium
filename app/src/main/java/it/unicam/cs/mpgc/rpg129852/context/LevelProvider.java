package it.unicam.cs.mpgc.rpg129852.context;

import it.unicam.cs.mpgc.rpg129852.model.Level;

/**
 * Fornisce l'accesso in sola lettura al livello attualmente in esecuzione.
 */
public interface LevelProvider {

    /**
     * Restituisce il livello attualmente attivo.
     * @return il Level corrente
     * @throws IllegalStateException se nessun livello è in esecuzione
     */
    Level getCurrentLevel();

    boolean hasActiveLevel();
}