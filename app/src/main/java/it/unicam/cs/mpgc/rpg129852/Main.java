package it.unicam.cs.mpgc.rpg129852;

import it.unicam.cs.mpgc.rpg129852.asset.AssetRegistry;
import it.unicam.cs.mpgc.rpg129852.asset.DiscipleAsset;
import it.unicam.cs.mpgc.rpg129852.asset.DiscipleAssetRegistry;
import it.unicam.cs.mpgc.rpg129852.context.GameContextImpl;
import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.controller.DiscipleCreationController;
import it.unicam.cs.mpgc.rpg129852.controller.LoadGameController;
import it.unicam.cs.mpgc.rpg129852.controller.MainMenuController;
import it.unicam.cs.mpgc.rpg129852.navigation.SceneManager;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.persistence.AvailableSavesProvider;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129852.persistence.JsonGameRepository;
import it.unicam.cs.mpgc.rpg129852.service.*;
import it.unicam.cs.mpgc.rpg129852.util.*;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.naming.Name;
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

        GameRepository repository = new JsonGameRepository();

        GameSessionManager gameSessionManager = new GameContextImpl();

        GameStarter gameStarter = createGameStarter(repository);
        GameLoader gameLoader = createGameLoader(repository, gameSessionManager);

        AssetRegistry<DiscipleAsset> discipleAssetRegistry = createDiscipleAssetRegistry();

        ControllerFactory controllerFactory = new ControllerFactory();
        ViewRouter sceneManager = new SceneManager(primaryStage, controllerFactory);

        controllerFactory.register(MainMenuController.class,
                () -> new MainMenuController(sceneManager));

        controllerFactory.register(DiscipleCreationController.class,
                () -> new DiscipleCreationController(gameStarter, discipleAssetRegistry, JOBS, sceneManager));

        controllerFactory.register(LoadGameController.class,
                ()-> new LoadGameController(repository, gameLoader, sceneManager));

        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }

    private GameLoader createGameLoader(GameRepository repository, GameSessionManager gameSessionManager) {
        return new GameLoaderImpl(repository, gameSessionManager);
    }

    private GameStarter createGameStarter(GameRepository repository) {
        GameFactory gameFactory = new GameFactoryImpl();

        SyntaxValidator syntaxValidator = new SaveNameSyntaxValidator();
        SaveNameFallbackProvider fallbackProvider = new SaveNameFallbackProviderImpl();

        SaveNameResolver saveNameResolver = new SaveNameResolverImpl(repository, syntaxValidator, fallbackProvider);

        return new GameStarterImpl(repository, gameFactory, saveNameResolver);
    }

    private AssetRegistry<DiscipleAsset> createDiscipleAssetRegistry() {
        AssetRegistry<DiscipleAsset> registry = new DiscipleAssetRegistry();
        registry.loadAssets(DISCIPLE_ASSETS_PATH);
        return registry;
    }

    public static void main(String[] args) {
        launch(args);
    }
}