package it.unicam.cs.mpgc.rpg129852.context;

import it.unicam.cs.mpgc.rpg129852.model.Game;

public class GameContextImpl implements GameSessionManager {

    private Game currentGame;

    @Override
    public Game getCurrentGame() {
        if (currentGame == null) {
            throw new IllegalStateException("No active game in the context.");
        }
        return currentGame;
    }

    @Override
    public void setCurrentGame(Game game) {
        this.currentGame = game;
    }

    @Override
    public boolean hasActiveGame() {
        return currentGame != null;
    }

    @Override
    public void clearSession() {
        this.currentGame = null;
    }
}