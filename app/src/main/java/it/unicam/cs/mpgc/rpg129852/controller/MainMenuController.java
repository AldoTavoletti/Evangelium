package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML
    private Button loadGameButton;

    @FXML
    private Button newGameButton;

    @FXML
    private Button quitButton;

    private final ViewRouter sceneManager;

    public MainMenuController(ViewRouter sceneManager) {
        this.sceneManager = sceneManager;
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
