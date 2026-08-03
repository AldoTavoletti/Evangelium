package it.unicam.cs.mpgc.rpg129852.context;

import it.unicam.cs.mpgc.rpg129852.model.Level;

/**
 * Gestisce il ciclo di vita del livello attivo, permettendo
 * di impostare un nuovo livello o terminarlo.
 */
public interface LevelSessionManager extends LevelProvider {

    void setCurrentLevel(Level level);

    void clearSession(); // Utile per pulire quando si torna al menu
}