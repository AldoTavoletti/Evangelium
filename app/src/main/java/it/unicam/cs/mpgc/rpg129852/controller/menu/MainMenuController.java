package it.unicam.cs.mpgc.rpg129852.controller.menu;

import it.unicam.cs.mpgc.rpg129852.context.game.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import java.util.Objects;

/**
 * Controller for the main menu view.
 * Handles the initial interactions of the player, such as choosing to start a new game,
 * to load an existing save, or to quit the application.
 */
public class MainMenuController {

    private final GameSessionManager gameSessionManager;
    private final ViewRouter sceneManager;

    /**
     * Constructs the main menu controller with its required dependencies.
     *
     * @param gameSessionManager the manager handling the active game session
     * @param sceneManager       the router responsible for switching views
     * @throws NullPointerException if any of the dependencies are null
     */
    public MainMenuController(GameSessionManager gameSessionManager, ViewRouter sceneManager) {
        this.gameSessionManager = Objects.requireNonNull(gameSessionManager, "The game session manager must not be null.");
        this.sceneManager = Objects.requireNonNull(sceneManager, "The scene manager must not be null.");
    }

    @FXML
    public void initialize() {
        gameSessionManager.clearSession();
    }

    @FXML
    void onNewGameAction() {
        sceneManager.switchScene(ViewRoute.DISCIPLE_CREATION);
    }

    @FXML
    void onLoadGameAction() {
        sceneManager.switchScene(ViewRoute.LOAD_GAME);
    }

    @FXML
    void onQuitAction() {
        Platform.exit();
    }
}