package it.unicam.cs.mpgc.rpg129852.context;

import it.unicam.cs.mpgc.rpg129852.model.Game;

public interface GameProvider {
    Game getCurrentGame();
    boolean hasActiveGame();
}