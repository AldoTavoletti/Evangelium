package it.unicam.cs.mpgc.rpg129852.bootstrap;

import com.google.gson.Gson;
import it.unicam.cs.mpgc.rpg129852.context.game.GameContextImpl;
import it.unicam.cs.mpgc.rpg129852.context.game.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.context.level.LevelContextImpl;
import it.unicam.cs.mpgc.rpg129852.context.level.LevelSessionManager;
import it.unicam.cs.mpgc.rpg129852.controller.menu.DiscipleCreationController;
import it.unicam.cs.mpgc.rpg129852.controller.menu.LoadGameController;
import it.unicam.cs.mpgc.rpg129852.controller.menu.MainMenuController;
import it.unicam.cs.mpgc.rpg129852.controller.session.GameplayController;
import it.unicam.cs.mpgc.rpg129852.controller.session.PlayerMenuController;
import it.unicam.cs.mpgc.rpg129852.controller.session.ShopController;
import it.unicam.cs.mpgc.rpg129852.controller.session.SummaryController;
import it.unicam.cs.mpgc.rpg129852.dto.book.Book;
import it.unicam.cs.mpgc.rpg129852.dto.disciple.DiscipleAsset;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.dto.level.ScriptureResource;
import it.unicam.cs.mpgc.rpg129852.navigation.SceneManager;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;
import it.unicam.cs.mpgc.rpg129852.persistence.game.GameRepository;
import it.unicam.cs.mpgc.rpg129852.service.book.BookCatalog;
import it.unicam.cs.mpgc.rpg129852.service.book.BookCatalogImpl;
import it.unicam.cs.mpgc.rpg129852.service.disciple.DiscipleProfileService;
import it.unicam.cs.mpgc.rpg129852.service.disciple.DiscipleProfileServiceImpl;
import it.unicam.cs.mpgc.rpg129852.service.game.GameStarter;
import it.unicam.cs.mpgc.rpg129852.service.level.gameplay.LevelStarter;
import it.unicam.cs.mpgc.rpg129852.service.level.gameplay.ScriptureCatalog;
import it.unicam.cs.mpgc.rpg129852.service.level.gameplay.ScriptureCatalogImpl;
import it.unicam.cs.mpgc.rpg129852.service.level.menu.*;
import it.unicam.cs.mpgc.rpg129852.service.shop.ShopService;
import it.unicam.cs.mpgc.rpg129852.service.shop.ShopServiceImpl;
import it.unicam.cs.mpgc.rpg129852.service.summary.StatsService;
import it.unicam.cs.mpgc.rpg129852.service.summary.StatsServiceImpl;
import it.unicam.cs.mpgc.rpg129852.service.summary.SummaryService;
import it.unicam.cs.mpgc.rpg129852.service.summary.SummaryServiceImpl;
import it.unicam.cs.mpgc.rpg129852.util.CircularListNavigator;
import it.unicam.cs.mpgc.rpg129852.util.CircularListNavigatorImpl;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * The Composition Root of the application.
 * This class is responsible for instantiating and wiring together all the dependencies,
 * services, and controllers needed to bootstrap and run the game.
 */
public class AppAssembler {

    private static final String APP_TITLE = "Evangelium";
    private static final String CHARACTERS_ASSETS_JSON = "/data/characters_assets.json";
    private static final String SCRIPTURE_JSON = "/data/scripture.json";
    private static final String BOOKS_JSON = "/data/books.json";
    private static final String LEVELS_METADATA_JSON = "/data/levels_metadata.json";

    /**
     * Assembles the application dependencies and starts the initial scene.
     *
     * @param primaryStage the primary window of the JavaFX application
     * @throws NullPointerException if the primary stage is null
     */
    public void assembleAndRun(Stage primaryStage) {
        Objects.requireNonNull(primaryStage, "The primary stage must not be null.");

        primaryStage.setTitle(APP_TITLE);
        primaryStage.setResizable(false);

        // persistency and context creation
        Gson gson = InfrastructureFactory.createGson();
        GameRepository repository = InfrastructureFactory.createGameRepository(gson);
        GameSessionManager gameSessionManager = new GameContextImpl();
        LevelSessionManager levelSessionManager = new LevelContextImpl();

        // registries creation
        ResourceRegistry<DiscipleAsset> discipleRegistry = InfrastructureFactory.loadRegistry(CHARACTERS_ASSETS_JSON, DiscipleAsset.class, gson);
        ResourceRegistry<ScriptureResource> scriptureRegistry = InfrastructureFactory.loadRegistry(SCRIPTURE_JSON, ScriptureResource.class, gson);
        ResourceRegistry<Book> bookRegistry = InfrastructureFactory.loadRegistry(BOOKS_JSON, Book.class, gson);
        ResourceRegistry<LevelMetadata> levelMetadataRegistry = InfrastructureFactory.loadRegistry(LEVELS_METADATA_JSON, LevelMetadata.class, gson);

        // catalogs creation
        BookCatalog bookCatalog = new BookCatalogImpl(bookRegistry);
        LevelCatalog levelCatalog = new LevelCatalogImpl(levelMetadataRegistry);
        ScriptureCatalog scriptureCatalog = new ScriptureCatalogImpl(scriptureRegistry);

        // services creation
        GameStarter gameStarter = DomainFactory.createGameStarter(repository, gameSessionManager);
        LevelStarter levelStarter = DomainFactory.createLevelStarter(levelSessionManager, gson);
        LevelBrowserService levelBrowser = new LevelBrowserServiceImpl(levelCatalog, bookCatalog, gameSessionManager);
        ShopService shopService = new ShopServiceImpl(bookCatalog, gameSessionManager, repository);
        SummaryService summaryService = new SummaryServiceImpl(gameSessionManager, levelCatalog);
        StatsService statsService = new StatsServiceImpl(gameSessionManager, levelCatalog);
        DiscipleProfileService discipleProfile = new DiscipleProfileServiceImpl(gameSessionManager, discipleRegistry);
        CircularListNavigator<DiscipleAsset> discipleNavigator = new CircularListNavigatorImpl<>(discipleRegistry.getAllResources());

        // controllers registration
        ControllerFactory controllerFactory = new ControllerFactory();
        ViewRouter sceneManager = new SceneManager(primaryStage, controllerFactory);

        controllerFactory.register(MainMenuController.class,
                () -> new MainMenuController(gameSessionManager, sceneManager));

        controllerFactory.register(DiscipleCreationController.class,
                () -> new DiscipleCreationController(gameStarter, discipleNavigator, sceneManager));

        controllerFactory.register(LoadGameController.class,
                () -> new LoadGameController(repository, gameSessionManager, sceneManager));

        controllerFactory.register(PlayerMenuController.class,
                () -> new PlayerMenuController(summaryService, discipleProfile, levelStarter, levelBrowser, sceneManager));

        controllerFactory.register(ShopController.class,
                () -> new ShopController(discipleProfile, shopService, sceneManager));

        controllerFactory.register(GameplayController.class,
                () -> new GameplayController(DomainFactory.createGameplayService(levelSessionManager, gameSessionManager, repository),
                        discipleProfile, scriptureCatalog, sceneManager));

        controllerFactory.register(SummaryController.class,
                () -> new SummaryController(statsService, discipleProfile, sceneManager));

        // start the application
        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }
}