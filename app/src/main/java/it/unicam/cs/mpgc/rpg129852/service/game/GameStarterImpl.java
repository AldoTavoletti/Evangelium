package it.unicam.cs.mpgc.rpg129852.service.game;

import it.unicam.cs.mpgc.rpg129852.context.game.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.game.NewGameRequest;
import it.unicam.cs.mpgc.rpg129852.model.game.Game;
import it.unicam.cs.mpgc.rpg129852.persistence.game.GameRepository;
import it.unicam.cs.mpgc.rpg129852.service.save.SaveNameResolver;

import java.util.Objects;

/**
 * Concrete implementation of the {@link GameStarter} interface.
 * It uses several services to ensure a new game is created, saved with a valid name, and activated as the current game context.
 */
public class GameStarterImpl implements GameStarter {

    private final GameRepository repository;
    private final GameSessionManager gameSessionManager;
    private final GameFactory gameFactory;
    private final SaveNameResolver saveNameResolver;

    /**
     * Constructs a new game starter.
     *
     * @param repository the repo used to save the new game
     * @param gameSessionManager the session manage to activate the new game as the current game
     * @param gameFactory the factory to create the new game
     * @param saveNameResolver the resolver to ensure the validity of the save name
     */
    public GameStarterImpl(GameRepository repository, GameSessionManager gameSessionManager, GameFactory gameFactory,
                           SaveNameResolver saveNameResolver) {
        this.gameFactory = Objects.requireNonNull(gameFactory, "The provided game factory must not be null.");
        this.repository = Objects.requireNonNull(repository, "The provided repository must not be null.");
        this.gameSessionManager = Objects.requireNonNull(gameSessionManager, "The provided game session manager must not be null.");
        this.saveNameResolver = Objects.requireNonNull(saveNameResolver, "The provided save name resolver must not be null.");
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
        Objects.requireNonNull(request, "The provided new game request must not be null.");

        String finalSaveName = saveNameResolver.resolveFinalName(request.saveName(), forceOverwrite);

        NewGameRequest updatedRequest = new NewGameRequest(request.discipleName(), request.job(), request.color(), finalSaveName);

        Game game = gameFactory.create(updatedRequest);

        repository.save(game);

        gameSessionManager.setCurrentGame(game);
    }

}
