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
import it.unicam.cs.mpgc.rpg129852.service.game.*;
import it.unicam.cs.mpgc.rpg129852.service.level.*;
import it.unicam.cs.mpgc.rpg129852.service.save.*;
import it.unicam.cs.mpgc.rpg129852.util.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main extends Application {

    // Configuration Constants
    private static final String APP_TITLE = "Evangelium";
    private static final String SAVE_FOLDER_NAME = ".evangelium";
    private static final String SAVES_SUBFOLDER = "saves";

    // Resource Paths
    private static final String DISCIPLE_ASSETS_PATH = "/disciples_assets.json";
    private static final String SCRIPTURE_ASSETS_PATH = "/scripture.json";
    private static final String BOOKS_ASSETS_PATH = "/books.json";
    private static final String LEVELS_METADATA_PATH = "/levels_metadata.json";

    private static final List<String> DISCIPLE_JOBS = List.of(
            "Pescatore", "Falegname", "Esattore delle imposte",
            "Fabbricante di tende", "Contadino", "Fabbro", "Medico"
    );

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setResizable(false);

        // 1. Core Config & Persistence
        Gson gson = configureGson();
        GameRepository repository = createGameRepository(gson);

        // 2. Data Registries (JSON)
        ResourceRegistry<DiscipleAsset> discipleRegistry = loadRegistry(DISCIPLE_ASSETS_PATH, DiscipleAsset.class, gson);
        ResourceRegistry<ScriptureResource> scriptureRegistry = loadRegistry(SCRIPTURE_ASSETS_PATH, ScriptureResource.class, gson);
        ResourceRegistry<Book> bookRegistry = loadRegistry(BOOKS_ASSETS_PATH, Book.class, gson);
        LevelCatalog levelCatalog = createLevelCatalog(gson);

        // 3. Context Managers (State)
        GameSessionManager gameSessionManager = new GameContextImpl();
        LevelSessionManager levelSessionManager = new LevelContextImpl();

        // 4. Domain Services
        BookCatalog bookCatalog = new BookCatalogImpl(bookRegistry);
        ScriptureCatalog scriptureCatalog = new ScriptureCatalogImpl(scriptureRegistry);
        GameLoader gameLoader = new GameLoaderImpl(repository, gameSessionManager);
        GameStarter gameStarter = createGameStarter(repository, gameSessionManager);
        LevelStarter levelStarter = createLevelStarter(levelSessionManager, gson);

        LevelBrowserService levelBrowser = new LevelBrowserServiceImpl(levelCatalog, bookCatalog, gameSessionManager);
        DiscipleProfileService discipleProfile = new DiscipleProfileServiceImpl(gameSessionManager, discipleRegistry);
        ShopService shopService = new ShopServiceImpl(bookCatalog, gameSessionManager, repository);

        CircularListNavigator<DiscipleAsset> discipleNavigator = new CircularListNavigatorImpl<>(discipleRegistry.getAllResources());

        // 5. Routing and Controllers
        ControllerFactory controllerFactory = new ControllerFactory();
        ViewRouter sceneManager = new SceneManager(primaryStage, controllerFactory);

        registerControllers(controllerFactory, sceneManager, gameSessionManager, levelSessionManager, repository,
                gameStarter, gameLoader, levelStarter, levelBrowser, discipleProfile, shopService,
                discipleRegistry, scriptureCatalog, discipleNavigator);

        // 6. App Startup
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }

    private Gson configureGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Inventory.class, (JsonDeserializer<Inventory>) (json, typeOfT, context) ->
                        context.deserialize(json, InventoryImpl.class))
                .create();
    }

    private GameRepository createGameRepository(Gson gson) {
        String userHome = System.getProperty("user.home");
        Path saveDirectory = Paths.get(userHome, SAVE_FOLDER_NAME, SAVES_SUBFOLDER);
        return new JsonGameRepository(saveDirectory, gson);
    }

    // Generic method to avoid code duplication when loading registries
    private <T extends Resource> ResourceRegistry<T> loadRegistry(String path, Class<T> type, Gson gson) {
        ResourceRegistry<T> registry = new ResourceRegistryImpl<>(path, type, gson);
        registry.loadResources();
        return registry;
    }

    private LevelCatalog createLevelCatalog(Gson gson) {
        ResourceRegistry<LevelMetadata> registry = loadRegistry(LEVELS_METADATA_PATH, LevelMetadata.class, gson);
        return new LevelCatalogImpl(registry);
    }

    private GameStarter createGameStarter(GameRepository repository, GameSessionManager sessionManager) {
        SyntaxValidator syntaxValidator = new SaveNameSyntaxValidator();
        SaveNameFallbackProvider fallbackProvider = new SaveNameFallbackProviderImpl();
        SaveNameResolver saveNameResolver = new SaveNameResolverImpl(repository, syntaxValidator, fallbackProvider);
        GameFactory gameFactory = new GameFactoryImpl();

        return new GameStarterImpl(repository, sessionManager, gameFactory, saveNameResolver);
    }

    private LevelStarter createLevelStarter(LevelSessionManager levelSessionManager, Gson gson) {
        ScenarioLoader scenarioLoader = new ScenarioLoaderImpl(gson);
        return new LevelStarterImpl(levelSessionManager, scenarioLoader);
    }

    private void registerControllers(ControllerFactory factory, ViewRouter sceneManager,
                                     GameSessionManager gameSessionManager, LevelSessionManager levelSessionManager,
                                     GameRepository repository, GameStarter gameStarter, GameLoader gameLoader,
                                     LevelStarter levelStarter, LevelBrowserService levelBrowser,
                                     DiscipleProfileService discipleProfile, ShopService shopService,
                                     ResourceRegistry<DiscipleAsset> discipleRegistry,
                                     ScriptureCatalog scriptureCatalog,
                                     CircularListNavigator<DiscipleAsset> discipleNavigator) {

        factory.register(MainMenuController.class,
                () -> new MainMenuController(gameSessionManager, sceneManager));

        factory.register(DiscipleCreationController.class,
                () -> new DiscipleCreationController(gameStarter, discipleNavigator, DISCIPLE_JOBS, sceneManager));

        factory.register(LoadGameController.class,
                () -> new LoadGameController(repository, gameLoader, sceneManager));

        factory.register(PlayerMenuController.class,
                () -> new PlayerMenuController(discipleProfile, levelStarter, levelBrowser, sceneManager));

        factory.register(GameplayController.class,
                () -> new GameplayController(createGameplayService(levelSessionManager, gameSessionManager, repository),
                        discipleProfile, scriptureCatalog, sceneManager));

        factory.register(ShopController.class,
                () -> new ShopController(shopService, sceneManager));
    }

    private GameplayService createGameplayService(LevelSessionManager levelSessionManager, GameSessionManager gameSessionManager, GameRepository repository) {
        LevelSaver levelSaver = new LevelSaverImpl(gameSessionManager, repository);
        LevelRewardsCalculator rewardsCalculator = new LevelRewardsCalculatorImpl();
        Level currentLevel = levelSessionManager.getCurrentLevel();
        LevelEngine levelEngine = new LevelEngineImpl(currentLevel.scenario().phases());

        return new GameplayServiceImpl(levelSessionManager, levelSaver, rewardsCalculator, levelEngine);
    }

    public static void main(String[] args) {
        launch(args);
    }
}