package it.unicam.cs.mpgc.rpg129852.bootstrap;

import com.google.common.math.Stats;
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
import it.unicam.cs.mpgc.rpg129852.service.ControllerFactory;
import it.unicam.cs.mpgc.rpg129852.service.book.BookCatalog;
import it.unicam.cs.mpgc.rpg129852.service.book.BookCatalogImpl;
import it.unicam.cs.mpgc.rpg129852.service.disciple.DiscipleProfileService;
import it.unicam.cs.mpgc.rpg129852.service.disciple.DiscipleProfileServiceImpl;
import it.unicam.cs.mpgc.rpg129852.service.game.GameLoader;
import it.unicam.cs.mpgc.rpg129852.service.game.GameLoaderImpl;
import it.unicam.cs.mpgc.rpg129852.service.game.GameStarter;
import it.unicam.cs.mpgc.rpg129852.service.level.gameplay.LevelStarter;
import it.unicam.cs.mpgc.rpg129852.service.level.gameplay.ScriptureCatalog;
import it.unicam.cs.mpgc.rpg129852.service.level.gameplay.ScriptureCatalogImpl;
import it.unicam.cs.mpgc.rpg129852.service.level.menu.*;
import it.unicam.cs.mpgc.rpg129852.service.shop.ShopService;
import it.unicam.cs.mpgc.rpg129852.service.shop.ShopServiceImpl;
import it.unicam.cs.mpgc.rpg129852.util.CircularListNavigator;
import it.unicam.cs.mpgc.rpg129852.util.CircularListNavigatorImpl;
import javafx.stage.Stage;

import java.util.List;

public class AppAssembler {

    private static final String APP_TITLE = "Evangelium";
    private static final String DISCIPLE_ASSETS_PATH = "/data/disciples_assets.json";
    private static final String SCRIPTURE_ASSETS_PATH = "/data/scripture.json";
    private static final String BOOKS_ASSETS_PATH = "/data/books.json";
    private static final String LEVELS_METADATA_PATH = "/data/levels_metadata.json";

    public void assembleAndRun(Stage primaryStage) {

        Gson gson = InfrastructureFactory.createGson();
        GameRepository repository = InfrastructureFactory.createGameRepository(gson);

        ResourceRegistry<DiscipleAsset> discipleRegistry = InfrastructureFactory.loadRegistry(DISCIPLE_ASSETS_PATH, DiscipleAsset.class, gson);
        ResourceRegistry<ScriptureResource> scriptureRegistry = InfrastructureFactory.loadRegistry(SCRIPTURE_ASSETS_PATH, ScriptureResource.class, gson);
        ResourceRegistry<Book> bookRegistry = InfrastructureFactory.loadRegistry(BOOKS_ASSETS_PATH, Book.class, gson);
        ResourceRegistry<LevelMetadata> levelMetadataRegistry = InfrastructureFactory.loadRegistry(LEVELS_METADATA_PATH, LevelMetadata.class, gson);

        GameSessionManager gameSessionManager = new GameContextImpl();
        LevelSessionManager levelSessionManager = new LevelContextImpl();

        BookCatalog bookCatalog = new BookCatalogImpl(bookRegistry);
        LevelCatalog levelCatalog = new LevelCatalogImpl(levelMetadataRegistry);
        ScriptureCatalog scriptureCatalog = new ScriptureCatalogImpl(scriptureRegistry);

        GameLoader gameLoader = new GameLoaderImpl(repository, gameSessionManager);
        GameStarter gameStarter = DomainFactory.createGameStarter(repository, gameSessionManager);
        LevelStarter levelStarter = DomainFactory.createLevelStarter(levelSessionManager, gson);

        LevelBrowserService levelBrowser = new LevelBrowserServiceImpl(levelCatalog, bookCatalog, gameSessionManager);
        DiscipleProfileService discipleProfile = new DiscipleProfileServiceImpl(gameSessionManager, discipleRegistry);
        ShopService shopService = new ShopServiceImpl(bookCatalog, gameSessionManager, repository);
        SummaryService summaryService = new SummaryServiceImpl(gameSessionManager, levelCatalog);
        StatsService statsService = new StatsServiceImpl(gameSessionManager, levelCatalog);

        CircularListNavigator<DiscipleAsset> discipleNavigator = new CircularListNavigatorImpl<>(discipleRegistry.getAllResources());

        primaryStage.setTitle(APP_TITLE);
        primaryStage.setResizable(false);

        ControllerFactory controllerFactory = new ControllerFactory();
        ViewRouter sceneManager = new SceneManager(primaryStage, controllerFactory);

        registerControllers(controllerFactory, sceneManager, gameSessionManager, levelSessionManager, repository,
                gameStarter, gameLoader, levelStarter, levelBrowser, discipleProfile, shopService,
                scriptureCatalog, discipleNavigator, summaryService, statsService);

        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }

    private void registerControllers(ControllerFactory factory, ViewRouter sceneManager,
                                     GameSessionManager gameSession, LevelSessionManager levelSession,
                                     GameRepository repository, GameStarter gameStarter, GameLoader gameLoader,
                                     LevelStarter levelStarter, LevelBrowserService levelBrowser,
                                     DiscipleProfileService discipleProfile, ShopService shopService,
                                     ScriptureCatalog scriptureCatalog, CircularListNavigator<DiscipleAsset> discipleNavigator,  SummaryService summaryService, StatsService statsService) {

        factory.register(MainMenuController.class,
                () -> new MainMenuController(gameSession, sceneManager));

        factory.register(DiscipleCreationController.class,
                () -> new DiscipleCreationController(gameStarter, discipleNavigator, sceneManager));

        factory.register(LoadGameController.class,
                () -> new LoadGameController(repository, gameLoader, sceneManager));

        factory.register(PlayerMenuController.class,
                () -> new PlayerMenuController(summaryService, discipleProfile, levelStarter, levelBrowser, sceneManager));

        factory.register(ShopController.class,
                () -> new ShopController(shopService, sceneManager));

        factory.register(GameplayController.class,
                () -> new GameplayController(DomainFactory.createGameplayService(levelSession, gameSession, repository),
                        discipleProfile, scriptureCatalog, sceneManager));

        factory.register(SummaryController.class,
                () -> new SummaryController(statsService, discipleProfile, sceneManager));
    }
}