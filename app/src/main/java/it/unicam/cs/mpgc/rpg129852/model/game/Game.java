package it.unicam.cs.mpgc.rpg129852.model.game;

import java.util.Objects;

/**
 * Represents a complete game session, bundling the save file identifier
 * with the mutable state of the player's progress.
 *
 * @param saveName  the unique identifier (filename) for this specific playthrough
 * @param gameState the current state of the player's progress, inventory, and stats
 */
public record Game(String saveName, GameState gameState) {

    /**
     * Compact constructor to ensure the game is valid upon creation.
     *
     * @throws IllegalArgumentException if the save name is null or blank
     * @throws NullPointerException     if the game state is null
     */
    public Game {
        if (saveName == null || saveName.trim().isEmpty()) {
            throw new IllegalArgumentException("The save name must not be null or blank.");
        }
        Objects.requireNonNull(gameState, "The game state must not be null.");
    }
}