package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.GameState;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;

public class LevelSaverImpl implements LevelSaver {

    private final GameSessionManager gameSessionManager;
    private final GameRepository repository;

    public LevelSaverImpl(GameSessionManager gameSessionManager, GameRepository repository) {
        this.gameSessionManager = gameSessionManager;
        this.repository = repository;
    }

    @Override
    public void save(String levelId, Virtues obtainedVirtues) {
        Game currentGame = gameSessionManager.getCurrentGame();
        GameState currentGameState = currentGame.gameState();

        currentGameState.recordLevelScore(levelId, obtainedVirtues);

        repository.save(currentGame);
    }
}
