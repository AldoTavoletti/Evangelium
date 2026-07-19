package it.unicam.cs.mpgc.rpg129852.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML
    private Button LoadGameButton;

    @FXML
    private Button NewGameButton;

    @FXML
    private Button QuitButton;

    @FXML
    void loadGameButtonPressed(ActionEvent event) {
        System.out.println("Loading game...");
    }

    @FXML
    void newGameButtonPressed(ActionEvent event) {
        System.out.println("Starting new game...");
    }

    @FXML
    void quitButtonPressed(ActionEvent event) {
        System.out.println("Quitting...");
    }


}
