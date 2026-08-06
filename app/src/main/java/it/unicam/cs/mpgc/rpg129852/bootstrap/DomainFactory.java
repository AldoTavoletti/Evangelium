package it.unicam.cs.mpgc.rpg129852.bootstrap;

import com.google.gson.Gson;
import it.unicam.cs.mpgc.rpg129852.context.game.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.context.level.LevelSessionManager;
import it.unicam.cs.mpgc.rpg129852.model.level.Level;
import it.unicam.cs.mpgc.rpg129852.persistence.game.GameRepository;
import it.unicam.cs.mpgc.rpg129852.persistence.level.ScenarioLoader;
import it.unicam.cs.mpgc.rpg129852.persistence.level.ScenarioLoaderImpl;
import it.unicam.cs.mpgc.rpg129852.service.game.GameFactory;
import it.unicam.cs.mpgc.rpg129852.service.game.GameFactoryImpl;
import it.unicam.cs.mpgc.rpg129852.service.game.GameStarter;
import it.unicam.cs.mpgc.rpg129852.service.game.GameStarterImpl;
import it.unicam.cs.mpgc.rpg129852.service.level.gameplay.*;
import it.unicam.cs.mpgc.rpg129852.service.save.*;
import it.unicam.cs.mpgc.rpg129852.util.SyntaxValidator;

class DomainFactory {

    static GameStarter createGameStarter(GameRepository repository, GameSessionManager sessionManager) {
        SyntaxValidator syntaxValidator = new SaveNameSyntaxValidator();
        SaveNameFallbackProvider fallbackProvider = new SaveNameFallbackProviderImpl();
        SaveNameResolver saveNameResolver = new SaveNameResolverImpl(repository, syntaxValidator, fallbackProvider);
        GameFactory gameFactory = new GameFactoryImpl();

        return new GameStarterImpl(repository, sessionManager, gameFactory, saveNameResolver);
    }

    static LevelStarter createLevelStarter(LevelSessionManager levelSessionManager, Gson gson) {
        ScenarioLoader scenarioLoader = new ScenarioLoaderImpl(gson);
        return new LevelStarterImpl(levelSessionManager, scenarioLoader);
    }

    static GameplayService createGameplayService(LevelSessionManager levelSessionManager, GameSessionManager gameSessionManager, GameRepository repository) {
        LevelSaver levelSaver = new LevelSaverImpl(gameSessionManager, repository);
        LevelRewardsCalculator rewardsCalculator = new LevelRewardsCalculatorImpl();
        Level currentLevel = levelSessionManager.getCurrentLevel();
        LevelEngine levelEngine = new LevelEngineImpl(currentLevel.scenario().phases());

        return new GameplayServiceImpl(levelSessionManager, levelSaver, rewardsCalculator, levelEngine);
    }
}