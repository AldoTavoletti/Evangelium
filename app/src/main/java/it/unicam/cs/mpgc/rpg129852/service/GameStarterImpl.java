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

    @Override
    public void startNewGame(NewGameRequest request) {
        doStart(request, false);
    }

    @Override
    public void overwriteAndStartNewGame(NewGameRequest request) {
        doStart(request, true);
    }

    private void doStart(NewGameRequest request, boolean forceOverwrite) {
        String finalSaveName = saveNameResolver.resolveFinalName(request.saveName(), forceOverwrite);

        DiscipleData discipleData = new DiscipleData(request.discipleName(), request.job(), request.color());
        GameState gameState = new GameState(discipleData);
        Game game = new Game(finalSaveName, gameState);

        repository.save(game);
    }

}
