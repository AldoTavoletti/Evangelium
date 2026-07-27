package it.unicam.cs.mpgc.rpg129852.context;

import it.unicam.cs.mpgc.rpg129852.model.Game;

public interface GameSessionManager extends GameProvider {
    void setCurrentGame(Game game);
    void clearSession();
}