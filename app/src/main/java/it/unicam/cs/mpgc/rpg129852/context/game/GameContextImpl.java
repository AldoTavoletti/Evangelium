package it.unicam.cs.mpgc.rpg129852.context.game;

import it.unicam.cs.mpgc.rpg129852.model.game.Game;

import java.util.Objects;

/**
 * Concrete implementation of the {@link GameSessionManager}.
 * It manages the runtime state of the active game session in memory,
 * allowing the application to track, retrieve, or clear the current game.
 */
public class GameContextImpl implements GameSessionManager {

    private Game currentGame;

    @Override
    public Game getCurrentGame() {
        if (!hasActiveGame()) {
            throw new IllegalStateException("No active game in the context.");
        }

        return currentGame;
    }

    @Override
    public void setCurrentGame(Game game) {
        this.currentGame = Objects.requireNonNull(game, "The game to set must not be null.");
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