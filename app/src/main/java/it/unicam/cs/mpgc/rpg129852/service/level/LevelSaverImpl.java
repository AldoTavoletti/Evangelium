package it.unicam.cs.mpgc.rpg129852.service.level;

import it.unicam.cs.mpgc.rpg129852.context.GameProvider;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.GameState;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;

public class LevelSaverImpl implements LevelSaver {

    private final GameProvider gameProvider;
    private final GameRepository repository;

    public LevelSaverImpl(GameProvider gameProvider, GameRepository repository) {
        this.gameProvider = gameProvider;
        this.repository = repository;
    }

    @Override
    public void save(String levelId, Virtues obtainedVirtues) {
        Game currentGame = gameProvider.getCurrentGame();
        GameState currentGameState = currentGame.gameState();

        currentGameState.recordLevelScore(levelId, obtainedVirtues);

        repository.save(currentGame);
    }
}
