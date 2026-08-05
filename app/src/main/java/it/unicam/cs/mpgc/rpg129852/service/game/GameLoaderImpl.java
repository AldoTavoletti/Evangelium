package it.unicam.cs.mpgc.rpg129852.service.game;

import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;

public class GameLoaderImpl implements GameLoader {

    private GameRepository repository;
    private GameSessionManager gameSessionManager;

    public GameLoaderImpl(GameRepository repository, GameSessionManager gameSessionManager) {
        this.repository = repository;
        this.gameSessionManager = gameSessionManager;
    }

    @Override
    public void loadGame(String saveName) {
        Game loadedGame = repository.load(saveName);
        gameSessionManager.setCurrentGame(loadedGame);
    }
}
