package it.unicam.cs.mpgc.rpg129852;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import it.unicam.cs.mpgc.rpg129852.context.*;
import it.unicam.cs.mpgc.rpg129852.controller.*;
import it.unicam.cs.mpgc.rpg129852.dto.*;
import it.unicam.cs.mpgc.rpg129852.model.Inventory;
import it.unicam.cs.mpgc.rpg129852.model.InventoryImpl;
import it.unicam.cs.mpgc.rpg129852.model.Level;
import it.unicam.cs.mpgc.rpg129852.navigation.SceneManager;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.persistence.*;
import it.unicam.cs.mpgc.rpg129852.service.*;
import it.unicam.cs.mpgc.rpg129852.util.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main extends Application {

    private static final String DISCIPLE_ASSETS_PATH = "/disciples_assets.json";
    private static final List<String> JOBS = List.of(
            "Pescatore", "Falegname", "Esattore delle imposte",
            "Fabbricante di tende", "Contadino", "Fabbro", "Medico"
    );

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Evangelium");

        String userHome = System.getProperty("user.home");
        Path saveDirectory = Paths.get(userHome, ".evangelium", "saves");

        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Inventory.class, (JsonDeserializer<Inventory>) (json, typeOfT, context) ->
                context.deserialize(json, InventoryImpl.class)).create();

        GameRepository repository = new JsonGameRepository(saveDirectory, gson);
        GameSessionManager gameSessionManager = new GameContextImpl();

        GameStarter gameStarter = createGameStarter(repository, gameSessionManager);
        GameLoader gameLoader = createGameLoader(repository, gameSessionManager);

        LevelSessionManager levelSessionManager = new LevelContextImpl();
        LevelStarter levelStarter = createLevelStarter(levelSessionManager, gson);

        ResourceRegistry<DiscipleAsset> discipleAssetRegistry = new ResourceRegistryImpl<>("/disciples_assets.json", DiscipleAsset.class, gson);
        discipleAssetRegistry.loadResources();

        ResourceRegistry<ScriptureResource> scriptureRegistry = new ResourceRegistryImpl<>("/scripture.json", ScriptureResource.class, gson);
        scriptureRegistry.loadResources();

        ResourceRegistry<Book> bookRegistry = new ResourceRegistryImpl<>("/books.json", Book.class, gson);
        bookRegistry.loadResources();

        BookCatalog bookCatalog = new BookCatalogImpl(bookRegistry);
        LevelCatalog levelCatalog = createLevelCatalog(gson);

        PlayerMenuService playerMenuService = new PlayerMenuServiceImpl(gameSessionManager, levelCatalog, levelStarter);

        ControllerFactory controllerFactory = new ControllerFactory();
        ViewRouter sceneManager = new SceneManager(primaryStage, controllerFactory);

        ShopService shopService = new ShopServiceImpl(gameSessionManager, repository);

        controllerFactory.register(MainMenuController.class,
                () -> new MainMenuController(gameSessionManager, sceneManager));

        controllerFactory.register(DiscipleCreationController.class,
                () -> new DiscipleCreationController(gameStarter, discipleAssetRegistry, JOBS, sceneManager));

        controllerFactory.register(LoadGameController.class,
                () -> new LoadGameController(repository, gameLoader, sceneManager));

        controllerFactory.register(PlayerMenuController.class,
                () -> new PlayerMenuController(playerMenuService, discipleAssetRegistry, sceneManager));

        controllerFactory.register(GameplayController.class,
                () -> new GameplayController(createGameplayService(levelSessionManager, gameSessionManager, repository), gameSessionManager, discipleAssetRegistry, scriptureRegistry, sceneManager));

        controllerFactory.register(ShopController.class,
                () -> new ShopController(bookCatalog, shopService, sceneManager));

        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }

    private GameplayService createGameplayService(LevelSessionManager levelContext, GameSessionManager gameSessionManager, GameRepository repository) {

        LevelSaver levelSaver = new LevelSaverImpl(gameSessionManager, repository);
        LevelRewardsCalculator rewardsCalculator = new LevelRewardsCalculatorImpl();
        Level currentLevel = levelContext.getCurrentLevel();
        LevelEngine levelEngine = new LevelEngineImpl(currentLevel.scenario().phases());

        return new GameplayServiceImpl(levelContext, levelSaver, rewardsCalculator, levelEngine);
    }

    private LevelCatalog createLevelCatalog(Gson gson) {
        ResourceRegistry<LevelMetadata> levelMetadataRegistry = new ResourceRegistryImpl<>("/levels_metadata.json", LevelMetadata.class, gson);
        levelMetadataRegistry.loadResources();
        return new LevelCatalogImpl(levelMetadataRegistry);
    }

    private LevelStarter createLevelStarter(LevelSessionManager levelContext, Gson gson) {
        ScenarioLoader scenarioLoader = new ScenarioLoaderImpl(gson);
        return new LevelStarterImpl(levelContext, scenarioLoader);
    }

    private GameLoader createGameLoader(GameRepository repository, GameSessionManager gameSessionManager) {
        return new GameLoaderImpl(repository, gameSessionManager);
    }

    private GameStarter createGameStarter(GameRepository repository, GameSessionManager sessionManager) {
        SyntaxValidator syntaxValidator = new SaveNameSyntaxValidator();
        SaveNameFallbackProvider fallbackProvider = new SaveNameFallbackProviderImpl();
        SaveNameResolver saveNameResolver = new SaveNameResolverImpl(repository, syntaxValidator, fallbackProvider);

        GameFactory gameFactory = new GameFactoryImpl();

        return new GameStarterImpl(repository, sessionManager, gameFactory, saveNameResolver);
    }

    public static void main(String[] args) {
        launch(args);
    }
}