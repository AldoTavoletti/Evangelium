package it.unicam.cs.mpgc.rpg129852.context.level;

import it.unicam.cs.mpgc.rpg129852.model.level.Level;

/**
 * Gestisce il ciclo di vita del livello attivo, permettendo
 * di impostare un nuovo livello o terminarlo.
 */
public interface LevelSessionManager extends LevelProvider {
    void setCurrentLevel(Level level);
    void clearSession();
}