package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.model.*;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;

public class GameStarterImpl implements GameStarter {

    private GameRepository repository;
    private GameFactory gameFactory;

    public GameStarterImpl(GameRepository repository, GameFactory gameFactory) {
        this.repository = repository;
        this.gameFactory = gameFactory;
    }

    public void startNewGame(String discipleName, String discipleJob, String color, String savePath) {
        DiscipleData discipleData = new DiscipleData(discipleName, discipleJob, color);
        GameState gameState = new GameState(discipleData);
        Game game = gameFactory.create(savePath, gameState);

        repository.save(game);
    }

}
