package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;
import it.unicam.cs.mpgc.rpg129852.model.game.Game;
import it.unicam.cs.mpgc.rpg129852.model.game.GameState;
import it.unicam.cs.mpgc.rpg129852.model.game.GameStateImpl;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.persistence.game.GameRepository;

import java.util.Objects;

/**
 * Concrete implementation of the {@link LevelSaver}.
 * It updates the current game state with the new score and delegates the physical
 * data persistence to the underlying game repository.
 */
public class LevelSaverImpl implements LevelSaver {

    private final GameProvider gameProvider;
    private final GameRepository repository;

    /**
     * Constructs a new level saver.
     *
     * @param gameProvider the provider granting access to the active game session
     * @param repository   the persistence component responsible for saving the game data
     * @throws NullPointerException if any of the dependencies are null
     */
    public LevelSaverImpl(GameProvider gameProvider, GameRepository repository) {
        this.gameProvider = Objects.requireNonNull(gameProvider, "The game provider must not be null.");
        this.repository = Objects.requireNonNull(repository, "The game repository must not be null.");
    }

    @Override
    public void save(String levelId, Score score) {
        if (levelId == null || levelId.trim().isEmpty()) {
            throw new IllegalArgumentException("The level ID must not be null or empty.");
        }
        Objects.requireNonNull(score, "The level score must not be null.");

        Game currentGame = gameProvider.getCurrentGame();
        GameState currentGameState = currentGame.gameState();

        currentGameState.recordLevelScore(levelId, score);

        repository.save(currentGame);
    }
}