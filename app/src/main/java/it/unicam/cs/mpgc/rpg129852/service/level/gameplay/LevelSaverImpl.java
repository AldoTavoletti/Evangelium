package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;
import it.unicam.cs.mpgc.rpg129852.model.game.Game;
import it.unicam.cs.mpgc.rpg129852.model.game.GameState;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;
import it.unicam.cs.mpgc.rpg129852.persistence.game.GameRepository;

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
