package it.unicam.cs.mpgc.rpg129852;

import it.unicam.cs.mpgc.rpg129852.asset.AssetRegistry;
import it.unicam.cs.mpgc.rpg129852.asset.DiscipleAsset;
import it.unicam.cs.mpgc.rpg129852.asset.DiscipleAssetRegistry;
import it.unicam.cs.mpgc.rpg129852.controller.DiscipleCreationController;
import it.unicam.cs.mpgc.rpg129852.controller.MainMenuController;
import it.unicam.cs.mpgc.rpg129852.navigation.SceneManager;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg129852.persistence.JsonGameRepository;
import it.unicam.cs.mpgc.rpg129852.service.*;
import javafx.application.Application;
import javafx.stage.Stage;

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

        GameStarter gameStarter = createGameStarter(repository);
        AssetRegistry<DiscipleAsset> discipleAssetRegistry = createDiscipleAssetRegistry();

        ControllerFactory controllerFactory = new ControllerFactory();
        ViewRouter sceneManager = new SceneManager(primaryStage, controllerFactory);

        controllerFactory.register(MainMenuController.class,
                () -> new MainMenuController(sceneManager));

        controllerFactory.register(DiscipleCreationController.class,
                () -> new DiscipleCreationController(gameStarter, discipleAssetRegistry, JOBS, sceneManager));

        sceneManager.switchScene(ViewRoute.MAIN_MENU);
    }

    private GameStarter createGameStarter(GameRepository repository) {
        GameFactory gameFactory = new GameFactoryImpl();
        return new GameStarterImpl(repository, gameFactory);
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