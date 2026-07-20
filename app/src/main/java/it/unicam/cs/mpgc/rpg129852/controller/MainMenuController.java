package it.unicam.cs.mpgc.rpg129852.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import static it.unicam.cs.mpgc.rpg129852.util.SceneUtils.switchScene;

public class MainMenuController {

    @FXML
    private Button loadGameButton;

    @FXML
    private Button newGameButton;

    @FXML
    private Button quitButton;

    @FXML
    void onNewGameAction(ActionEvent event) {
        switchScene("/view/DiscipleCreation.fxml", event);
    }

    @FXML
    void onLoadGameAction(ActionEvent event) {
        System.out.println("Loading game...");
    }

    @FXML
    void onQuitAction(ActionEvent event) {
        System.out.println("Quitting...");
    }

}
