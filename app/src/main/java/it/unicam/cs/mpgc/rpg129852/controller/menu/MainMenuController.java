package it.unicam.cs.mpgc.rpg129852.controller.menu;

import it.unicam.cs.mpgc.rpg129852.context.game.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    private final GameSessionManager gameSessionManager;
    private final ViewRouter sceneManager;

    @FXML
    private Button loadGameButton;

    @FXML
    private Button newGameButton;

    @FXML
    private Button quitButton;

    public MainMenuController(GameSessionManager gameSessionManager, ViewRouter sceneManager) {
        this.sceneManager = sceneManager;
        this.gameSessionManager = gameSessionManager;
    }

    @FXML
    public void initialize() {
        gameSessionManager.clearSession();
    }

    @FXML
    void onNewGameAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.DISCIPLE_CREATION);
    }

    @FXML
    void onLoadGameAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.LOAD_GAME);
    }

    @FXML
    void onQuitAction(ActionEvent event) {
        javafx.application.Platform.exit();
    }

}
