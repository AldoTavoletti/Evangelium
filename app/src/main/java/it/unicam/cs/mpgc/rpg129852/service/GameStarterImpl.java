package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.model.*;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129852.util.SaveNameResolver;

public class GameStarterImpl implements GameStarter {

    private final GameRepository repository;
    private final SaveNameResolver saveNameResolver;

    public GameStarterImpl(GameRepository repository,
                           SaveNameResolver saveNameResolver) {
        this.repository = repository;
        this.saveNameResolver = saveNameResolver;
    }

    public void startNewGame(String discipleName, String discipleJob, String color, String saveName, boolean forceOverwrite) {
        String finalSaveName = saveNameResolver.resolveFinalName(saveName, forceOverwrite);

        DiscipleData discipleData = new DiscipleData(discipleName, discipleJob, color);
        GameState gameState = new GameState(discipleData);
        Game game = new Game(finalSaveName, gameState);

        repository.save(game);
    }

}
