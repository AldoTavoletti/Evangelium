package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.model.*;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129852.util.SaveNameResolver;

public class GameStarterImpl implements GameStarter {

    private final GameRepository repository;
    private final SaveNameResolver saveNameResolver;
    private final GameSessionManager sessionManager;
    private final GameFactory gameFactory;


    public GameStarterImpl(GameRepository repository, GameSessionManager sessionManager, GameFactory gameFactory,
                           SaveNameResolver saveNameResolver) {
        this.gameFactory = gameFactory;
        this.repository = repository;
        this.sessionManager = sessionManager;
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

        NewGameRequest updatedRequest = new NewGameRequest(request.discipleName(), request.job(), request.color(), finalSaveName);

        Game game = gameFactory.create(updatedRequest);

        repository.save(game);

        sessionManager.setCurrentGame(game);
    }

}
