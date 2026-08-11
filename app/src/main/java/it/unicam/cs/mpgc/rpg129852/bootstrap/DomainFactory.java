package it.unicam.cs.mpgc.rpg129852.bootstrap;

import com.google.gson.Gson;
import it.unicam.cs.mpgc.rpg129852.context.game.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.context.level.LevelSessionManager;
import it.unicam.cs.mpgc.rpg129852.model.level.Level;
import it.unicam.cs.mpgc.rpg129852.persistence.game.GameRepository;
import it.unicam.cs.mpgc.rpg129852.persistence.level.ScenarioLoader;
import it.unicam.cs.mpgc.rpg129852.persistence.level.ScenarioLoaderImpl;
import it.unicam.cs.mpgc.rpg129852.service.game.*;
import it.unicam.cs.mpgc.rpg129852.service.level.gameplay.*;
import it.unicam.cs.mpgc.rpg129852.service.save.*;
import it.unicam.cs.mpgc.rpg129852.util.SyntaxValidator;

import java.util.Objects;

/**
 * Factory class responsible for instantiating complex domain services.
 * It encapsulates the creation logic and dependency wiring for the game's core systems.
 */
class DomainFactory {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DomainFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Creates a fully configured {@link GameStarter} instance.
     *
     * @param repository     the repository for saving and loading games
     * @param sessionManager the manager holding the current game session
     * @return a new GameStarter instance
     * @throws NullPointerException if any of the parameters are null
     */
    static GameStarter createGameStarter(GameRepository repository, GameSessionManager sessionManager) {
        Objects.requireNonNull(repository, "The repository must not be null.");
        Objects.requireNonNull(sessionManager, "The session manager must not be null.");

        SyntaxValidator syntaxValidator = new SaveNameSyntaxValidator();
        SaveNameFallbackProvider fallbackProvider = new SaveNameFallbackProviderImpl();
        SaveNameResolver saveNameResolver = new SaveNameResolverImpl(repository, syntaxValidator, fallbackProvider);
        GameFactory gameFactory = new GameFactoryImpl();

        return new GameStarterImpl(repository, sessionManager, gameFactory, saveNameResolver);
    }

    /**
     * Creates a fully configured {@link LevelStarter} instance.
     *
     * @param levelSessionManager the manager holding the current level session
     * @param gson                the JSON parser for loading scenario data
     * @return a new LevelStarter instance
     * @throws NullPointerException if any of the parameters are null
     */
    static LevelStarter createLevelStarter(LevelSessionManager levelSessionManager, Gson gson) {
        Objects.requireNonNull(levelSessionManager, "The level session manager must not be null.");
        Objects.requireNonNull(gson, "The Gson instance must not be null.");

        ScenarioLoader scenarioLoader = new ScenarioLoaderImpl(gson);
        return new LevelStarterImpl(levelSessionManager, scenarioLoader);
    }

    /**
     * Creates a fully configured {@link GameplayService} instance.
     * Note: This method must be called ONLY after a level has been successfully
     * set inside the LevelSessionManager, as it eagerly loads the current level scenario.
     *
     * @param levelSessionManager the manager holding the current level session
     * @param gameSessionManager  the manager holding the current game session
     * @param repository          the repository for saving game progress
     * @return a new GameplayService instance
     * @throws NullPointerException if any of the parameters are null
     * @throws IllegalStateException if there is no active level in the session manager
     */
    static GameplayService createGameplayService(LevelSessionManager levelSessionManager, GameSessionManager gameSessionManager, GameRepository repository) {
        Objects.requireNonNull(levelSessionManager, "The level session manager must not be null.");
        Objects.requireNonNull(gameSessionManager, "The game session manager must not be null.");
        Objects.requireNonNull(repository, "The repository must not be null.");

        LevelSaver levelSaver = new LevelSaverImpl(gameSessionManager, repository);
        LevelRewardsCalculator rewardsCalculator = new LevelRewardsCalculatorImpl();

        Level currentLevel = levelSessionManager.getCurrentLevel();
        LevelEngine levelEngine = new LevelEngineImpl(currentLevel.scenario().phases());

        return new GameplayServiceImpl(levelSessionManager, levelSaver, rewardsCalculator, levelEngine);
    }
}